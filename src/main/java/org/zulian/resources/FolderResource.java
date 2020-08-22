package org.zulian.resources;


import org.zulian.services.FolderService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/api/folder")
public class FolderResource {

    @Inject
    FolderService folderService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "hello";
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response test() {
        return Response
                .status(Response.Status.OK)
                .entity(folderService.testFolderService())
                .build();
    }
}
