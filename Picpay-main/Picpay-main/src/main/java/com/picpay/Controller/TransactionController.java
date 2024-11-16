package com.picpay.Controller;

import com.picpay.DTO.TransactionRequest;
import com.picpay.domain.transaction.Transaction;

import com.picpay.service.TransactionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/transactions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {

    @Inject
    TransactionService transactionService;

    @POST
    public Response createTransaction(TransactionRequest request) {
        try {
            Transaction transaction = transactionService.createTransaction(request.getSenderId(), request.getReceiverId(), request.getAmount());
            return Response.ok(transaction).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (BadRequestException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro inesperado ao processar sua solicitação.")
                    .build();
        }
    }
}
