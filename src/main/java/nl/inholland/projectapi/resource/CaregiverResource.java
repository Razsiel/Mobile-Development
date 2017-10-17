package nl.inholland.projectapi.resource;

import java.net.URI;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import nl.inholland.projectapi.model.Caregiver;
import nl.inholland.projectapi.model.Credentials;
import nl.inholland.projectapi.model.Role;
import nl.inholland.projectapi.model.Secured;
import nl.inholland.projectapi.presentation.CaregiverPresenter;
import nl.inholland.projectapi.presentation.model.CaregiverView;
import nl.inholland.projectapi.service.CaregiverService;
import org.bson.types.ObjectId;

@Path("/api/v1/caregivers")
@Secured({Role.admin, Role.caregiver})
public class CaregiverResource extends BaseResource {

    private final CaregiverService caregiverService;
    private final CaregiverPresenter caregiverPresenter;

    @Inject
    public CaregiverResource(CaregiverService caregiverService, CaregiverPresenter caregiverPresenter) {
        this.caregiverService = caregiverService;
        this.caregiverPresenter = caregiverPresenter;
    }

    @GET
    @Produces("application/json")
    public List<CaregiverView> getAll(@QueryParam("count") int count) {
        List<Caregiver> caregivers = caregiverService.getAll(count);

        if (count != 0) {
            List<Caregiver> reducedList = caregiverService.reduceList(caregivers, count);
            return caregiverPresenter.present(reducedList);
        }

        return caregiverPresenter.present(caregivers);
    }
    
    @POST
    @Consumes("application/json")
    @Secured({Role.admin})
    public Response create(Credentials credentials, @Context UriInfo uriInfo) {
        URI uri = caregiverService.create(credentials, uriInfo);
        return Response.created(uri).build();
    }
    
    @GET
    @Path("/{caregiverId}")
    @Produces("application/json")
    public CaregiverView getById(@PathParam("caregiverId") String caregiverId, @Context SecurityContext context) {
        Caregiver caregiver = caregiverService.getById(caregiverId, context.getUserPrincipal());
        return caregiverPresenter.present(caregiver);
    }
    
    @PUT
    @Path("/{caregiverId}")
    @Consumes("application/json")
    @Secured({Role.admin, Role.caregiver})
    public Response update(@PathParam("caregiverId") String caregiverId, Credentials credentials, @Context SecurityContext context) {
        caregiverService.update(caregiverId, credentials, context.getUserPrincipal());
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/{caregiverId}")
    @Secured({Role.admin})
    public void delete(@PathParam("caregiverId") ObjectId caregiverId) {
        caregiverService.deleteById(caregiverId);
    }
}
