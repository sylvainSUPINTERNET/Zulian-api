package org.zulian.resources;


import org.eclipse.microprofile.jwt.JsonWebToken;
import org.zulian.services.FolderService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@RequestScoped
@Path("/api/folder")
public class FolderResource {

    @Inject
    FolderService folderService;

    @Inject
    JsonWebToken jwt;


    @GET
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String ee(@Context SecurityContext ctx) {
        return "hello";
    }

    @GET
    @RolesAllowed({"User","Admin"})
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context SecurityContext ctx) {
        return "hello";
    }


    @POST
    @RolesAllowed({"User","Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response test(@Context SecurityContext ctx) {
        return Response
                .status(Response.Status.OK)
                .entity(folderService.testFolderService())
                .build();
    }
}
