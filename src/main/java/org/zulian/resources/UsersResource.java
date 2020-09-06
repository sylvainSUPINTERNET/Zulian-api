package org.zulian.resources;


import org.eclipse.microprofile.openapi.annotations.ExternalDocumentation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.zulian.DTO.UserCreateDto;
import org.zulian.services.UsersService;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Path("/api/v1/users")
public class UsersResource {
    @Inject
    UsersService usersService;

    @Inject
    UserCreateDto userCreateDto;


    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Tag(name = "Users", description = "CRUD Services for Users entity", externalDocs = @ExternalDocumentation(description = "Users CRUD endpoints"))
    public Response createUser(@Context SecurityContext ctx, UserCreateDto userCreateDto) {
        return Response
                .status(Response.Status.OK)
                .entity(usersService.createUser(userCreateDto))
                .build();
    }
}
