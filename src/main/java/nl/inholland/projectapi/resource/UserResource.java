package nl.inholland.projectapi.resource;

import io.swagger.annotations.Api;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import nl.inholland.projectapi.model.inputs.Credentials;
import nl.inholland.projectapi.service.UserService;
import nl.inholland.projectapi.model.Role;
import nl.inholland.projectapi.model.Secured;
import nl.inholland.projectapi.model.User;

@Api("General user operations")
@Path("/api/v1/")
public class UserResource extends BaseResource {

    private final UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(Credentials credentials) {
        userService.requireResult(credentials, "Json object in body required");
        User user = userService.login(credentials);
        return Response.ok()
                .entity(user.getApiKey())
                .header("userId", user.getId())
                .header("userRole", user.getRole())
                .build();
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Secured({Role.admin, Role.family, Role.client, Role.caregiver})
    public void logout(@Context SecurityContext context) {
        userService.logout(context.getUserPrincipal().getName());
    }
}
