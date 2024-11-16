package com.picpay.service;

import com.picpay.domain.user.UserEntity;

import com.picpay.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> findAll() {
        return userRepository.listAll();
    }

    public UserEntity findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public Response create(UserEntity userCreate) {
        try {
            long count = userRepository.findByDocument(userCreate.getDocument());

            if (count != 0)     {
                return Response.status(404)
                        .entity("Usuário com o documento " + userCreate.getDocument() + " já existe")
                        .build();
            }

            userRepository.persist(userCreate);
            return Response.status(Response.Status.CREATED).entity(userCreate).build();

        } catch (EntityExistsException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("Erro ao criar um novo usuário.: " + e.getMessage())
                       .build();
        }
    }

    @Transactional
    public UserEntity update(Long id, UserEntity user) {
        UserEntity entity = userRepository.findById(id);
        if (entity != null) {
            entity.setUserName(user.getUserName());
            entity.setDocument(user.getDocument());
            entity.setEmail(user.getEmail());
            entity.setPassword(user.getPassword());
            entity.setBalance(user.getBalance());
            entity.setUserType(user.getUserType());
            return entity;
        }
        return null;
    }

    @Transactional
    public void delete(Long id) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("Usuário com ID " + id + " não encontrado");
        }
        userRepository.delete(user);
    }

}
