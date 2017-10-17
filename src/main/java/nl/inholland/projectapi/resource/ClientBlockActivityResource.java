package nl.inholland.projectapi.resource;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import nl.inholland.projectapi.model.Role;
import nl.inholland.projectapi.model.Secured;
import nl.inholland.projectapi.presentation.PersonalActivityPresenter;
import nl.inholland.projectapi.presentation.model.PersonalActivityView;
import nl.inholland.projectapi.service.ClientBlockActivityService;
import nl.inholland.projectapi.service.ClientService;

@Path("/api/v1/clients/{clientId}/blocks/{blockId}/activities")
@Secured({Role.admin, Role.client, Role.caregiver})
public class ClientBlockActivityResource extends BaseResource {

    private final ClientBlockActivityService service;
    private final PersonalActivityPresenter presenter;
    private final ClientService clientService;
    
    @Inject
    public ClientBlockActivityResource(ClientBlockActivityService service, PersonalActivityPresenter presenter, ClientService clientService) {
        this.service = service;
        this.presenter = presenter;
        this.clientService = clientService;
    }
    
    @GET
    @Produces("application/json")
    public List<PersonalActivityView> getActivities(
            @PathParam("clientId") String clientId,
            @PathParam("blockId") String blockId,
            @Context SecurityContext context) {
        return presenter.present(service.getActivities(clientService.getById(clientId, context.getUserPrincipal()), blockId));
    }
    
    @GET
    @Path("/{activityId}")
    @Produces("application/json")
    public PersonalActivityView getActivity(
            @PathParam("clientId") String clientId,
            @PathParam("blockId") String blockId,
            @PathParam("activityId") String activityId,
            @Context SecurityContext context) {
        return presenter.present(service.getActivity(clientService.getById(clientId, context.getUserPrincipal()), blockId, activityId));
    }
}
