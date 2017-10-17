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
import nl.inholland.projectapi.model.Like;
import nl.inholland.projectapi.model.Role;
import nl.inholland.projectapi.model.Secured;
import nl.inholland.projectapi.presentation.LikePresenter;
import nl.inholland.projectapi.presentation.model.LikeView;
import nl.inholland.projectapi.service.ClientBlockActivityLikeService;
import nl.inholland.projectapi.service.ClientService;

@Path("/api/v1/clients/{clientId}/blocks/{blockId}/activities/{activityId}/likes")
@Secured({Role.admin, Role.client, Role.caregiver, Role.family})
public class ClientBlockActivityLikeResource extends BaseResource {

    private final ClientBlockActivityLikeService service;
    private final ClientService clientService;
    private final LikePresenter presenter;

    @Inject
    public ClientBlockActivityLikeResource(ClientBlockActivityLikeService service, ClientService clientService, LikePresenter presenter) {
        this.service = service;
        this.presenter = presenter;
        this.clientService = clientService;
    }
    
    @GET
    @Produces("application/json")
    public List<LikeView> getAll(
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
            Like like,
            @Context UriInfo uriInfo,
            @Context SecurityContext context) {
        URI uri = service.create(clientService.getById(clientId, context.getUserPrincipal()), blockId, activityId, like, uriInfo);
        return Response.created(uri).build();
    }
    
    @GET
    @Path("/{likeId}")
    @Produces("application/json")
    public LikeView get(
            @PathParam("clientId") String clientId,
            @PathParam("blockId") String blockId,
            @PathParam("activityId") String activityId,
            @PathParam("likeId") String likeId,
            @Context SecurityContext context) {
        return presenter.present(service.get(clientService.getById(clientId, context.getUserPrincipal()), blockId, activityId, likeId));
    }    
    
    @DELETE
    @Path("/{likeId}")
    public void delete(
            @PathParam("clientId") String clientId,
            @PathParam("blockId") String blockId,
            @PathParam("activityId") String activityId,
            @PathParam("likeId") String likeId,
            @Context SecurityContext context) {
        service.delete(clientService.getById(clientId, context.getUserPrincipal()), blockId, activityId, likeId);
    }        
    
}
