package nl.inholland.projectapi.resource;

import java.net.URI;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import nl.inholland.projectapi.model.Comment;
import nl.inholland.projectapi.model.Role;
import nl.inholland.projectapi.model.Secured;
import nl.inholland.projectapi.presentation.CommentPresenter;
import nl.inholland.projectapi.presentation.model.CommentView;
import nl.inholland.projectapi.service.ClientBlockActivityCommentService;
import nl.inholland.projectapi.service.ClientService;

@Path("/api/v1/clients/{clientId}/blocks/{blockId}/activities/{activityId}/comments")
@Secured({Role.admin, Role.client, Role.caregiver, Role.family})
public class ClientBlockActivityCommentResource extends BaseResource {
    
    private final ClientBlockActivityCommentService service;
    private final ClientService clientService;
    private final CommentPresenter presenter;

    @Inject
    public ClientBlockActivityCommentResource(ClientBlockActivityCommentService service, ClientService clientService, CommentPresenter presenter) {
        this.service = service;
        this.clientService = clientService;
        this.presenter = presenter;
    }
    
    @GET
    @Produces("application/json")
    public List<CommentView> getAll(
            @PathParam("clientId") String clientId,
            @PathParam("blockId") String blockId,
            @PathParam("activityId") String activityId,
            @Context SecurityContext context) {
        return presenter.present(service.getAll(clientService.getById(clientId, context.getUserPrincipal()), blockId, activityId));
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response create(
            @PathParam("clientId") String clientId,
            @PathParam("blockId") String blockId,
            @PathParam("activityId") String activityId,
            Comment comment,
            @Context UriInfo uriInfo,
            @Context SecurityContext context) {
        URI uri = service.create(clientService.getById(clientId, context.getUserPrincipal()), blockId, activityId, comment, uriInfo);
        return Response.created(uri).build();
    }
    
    @GET
    @Path("/{commentId}")
    @Produces("application/json")
    public CommentView get(
            @PathParam("clientId") String clientId,
            @PathParam("blockId") String blockId,
            @PathParam("activityId") String activityId,
            @PathParam("commentId") String commentId,
            @Context SecurityContext context) {
        return presenter.present(service.get(clientService.getById(clientId, context.getUserPrincipal()), blockId, activityId, commentId));
    }    
    
    @DELETE
    @Path("/{commentId}")
    public void delete(
            @PathParam("clientId") String clientId,
            @PathParam("blockId") String blockId,
            @PathParam("activityId") String activityId,
            @PathParam("commentId") String commentId,
            @Context SecurityContext context) {
        service.delete(clientService.getById(clientId, context.getUserPrincipal()), blockId, activityId, commentId);
    }

}
