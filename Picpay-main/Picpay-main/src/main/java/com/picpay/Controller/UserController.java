package com.picpay.Controller;

import com.picpay.domain.user.UserEntity;
import com.picpay.service.TransactionService;
import io.smallrye.common.constraint.NotNull;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.picpay.service.UserService;

import java.util.List;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    UserService userService;
    @Inject
    TransactionService transactionService;

    @GET
    public List<UserEntity> getAllUsers() {
        return userService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        UserEntity userEntity = userService.findById(id);
        return userEntity !=null ? Response.ok(userEntity).build() : Response.status(Response.Status.NOT_FOUND).build();
    }


    @POST
    @Transactional
    public Response createUser(@NotNull UserEntity newUser) {
        Response updateUser = userService.create(newUser);
        return Response.status(updateUser.getStatus()).entity(updateUser.getEntity()).build();
    }


    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, UserEntity userEntity) {
        UserEntity updateUser = userService.update(id, userEntity);
        return updateUser != null ? Response.ok(updateUser).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            userService.delete(id);
            return Response.noContent().build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            for (StackTraceElement element : e.getStackTrace()) {
                System.out.println(element);
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao deletar usuário: " + e.getMessage())
                    .build();
        }
    }
}