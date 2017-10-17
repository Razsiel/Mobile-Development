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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import nl.inholland.projectapi.model.BuildingBlock;
import nl.inholland.projectapi.model.Role;
import nl.inholland.projectapi.model.Secured;
import nl.inholland.projectapi.presentation.BlockPresenter;
import nl.inholland.projectapi.presentation.model.BlockView;
import nl.inholland.projectapi.service.ClientBlockService;
import nl.inholland.projectapi.service.ClientService;

@Path("/api/v1/clients/{clientId}/blocks")
public class ClientBlockResource extends BaseResource {

    private final ClientBlockService clientBlockService;
    private final BlockPresenter blockPresenter;
    private final ClientService clientService;
    
    @Inject
    public ClientBlockResource(ClientBlockService clientBlockService, ClientService clientService, BlockPresenter blockPresenter) {
        this.clientBlockService = clientBlockService;
        this.blockPresenter = blockPresenter;
        this.clientService = clientService;
    }

    @GET
    @Secured({Role.admin, Role.client, Role.caregiver, Role.family})
    @Produces("application/json")
    public List<BlockView> getAll(
            @PathParam("clientId") String clientId,
            @QueryParam("count") int count,
            @Context SecurityContext context) {
        List<BuildingBlock> blocks = clientBlockService.getAll(clientService.getById(clientId, context.getUserPrincipal()), count);
        return blockPresenter.present(blocks);
    }
    
    @POST
    @Secured({Role.admin, Role.client, Role.caregiver})
    @Consumes("application/json")
    public Response create(
            @PathParam("clientId") String clientId,
            @QueryParam("blockId") String blockId,
            @Context UriInfo uriInfo,
            @Context SecurityContext context) {
        URI uri = clientBlockService.create(clientService.getById(clientId, context.getUserPrincipal()), blockId, uriInfo);
        return Response.created(uri).build();
    }
    
    @GET
    @Secured({Role.admin, Role.client, Role.caregiver, Role.family})
    @Path("/{blockId}")
    @Produces("application/json")
    public BlockView get(
            @PathParam("clientId") String clientId,
            @PathParam("blockId") String blockId,
            @Context SecurityContext context) {
        return blockPresenter.present(clientBlockService.getById(clientService.getById(clientId, context.getUserPrincipal()), blockId));
    }
    
    @DELETE
    @Secured({Role.admin, Role.client, Role.caregiver})
    @Path("/{blockId}")
    @Produces("application/json")
    public void delete(
            @PathParam("clientId") String clientId,
            @PathParam("blockId") String blockId,
            @Context SecurityContext context) {
        clientBlockService.delete(clientService.getById(clientId, context.getUserPrincipal()), blockId);
    }    
}
