package com.picpay.service;

import com.picpay.Interface.AuthorizerClient;
import com.picpay.domain.AuthorizeResponse.AuthorizeResponse;
import com.picpay.domain.transaction.Transaction;
import com.picpay.domain.user.UserEntity;
import com.picpay.domain.user.UserType;
import com.picpay.repositories.TransactionRepository;
import com.picpay.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApplicationScoped
public class TransactionService {

    @Inject
    UserRepository userRepository;

    @Inject
    TransactionRepository transactionRepository;

    @Inject
    @RestClient
    AuthorizerClient authorizerClient;


    @Transactional
    public Transaction createTransaction(Long senderId, Long receiverId, BigDecimal amount) {
        UserEntity sender = userRepository.findById(senderId);
        UserEntity receiver = userRepository.findById(receiverId);

        if (sender == null) {
            throw new NotFoundException("Usuário remetente não encontrado.");
        }
        if (receiver == null) {
            throw new NotFoundException("Usuário ou lojista destinatário não encontrado.");
        }

        if (sender.getUserType() == UserType.MERCHANT) {
            throw new BadRequestException("Usuários do tipo Logistas não podem realizar transações.");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Saldo insuficiente.");
        }

        // Consulta ao serviço autorizador
            AuthorizeResponse response = authorizerClient.authorize(amount);
            if (!response.getData().isAuthorization()) {
            throw new BadRequestException("Transferência não autorizada.");
        }

        // Realiza a transferência
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        // Persistindo a transação
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.persist(transaction);
        return transaction;
    }
}