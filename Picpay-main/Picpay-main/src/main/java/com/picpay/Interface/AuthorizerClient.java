package com.picpay.Interface;

import com.picpay.domain.AuthorizeResponse.AuthorizeResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.math.BigDecimal;

@RegisterRestClient(baseUri = "https://util.devi.tools/api/v2")
@Path("/authorize")
public interface AuthorizerClient {

    @GET
    AuthorizeResponse authorize(@QueryParam("amount") BigDecimal amount);
}
