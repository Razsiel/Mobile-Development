package nl.inholland.projectapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.UriInfo;
import nl.inholland.projectapi.model.APIKey;
import nl.inholland.projectapi.model.BuildingBlock;
import nl.inholland.projectapi.model.Client;
import nl.inholland.projectapi.model.inputs.Credentials;
import nl.inholland.projectapi.persistence.BlockDAO;
import nl.inholland.projectapi.persistence.ClientDAO;
import nl.inholland.projectapi.persistence.UserDAO;
import org.bson.types.ObjectId;

public class ClientService extends BaseService {

    private final ClientDAO dao;
    private final UserDAO userDAO;
    private final BlockDAO blockDAO;

    @Inject
    public ClientService(ClientDAO dao, UserDAO userDAO, BlockDAO blockDAO) {
        this.dao = dao;
        this.userDAO = userDAO;
        this.blockDAO = blockDAO;
    }

    public List<Client> getAll(int count) {
        List<Client> clients = dao.getAllClients();
        requireResult(clients, "Clients not found");
        return reduceList(clients, count);
    }

    public URI create(Credentials credentials, UriInfo uriInfo) {
        if (userDAO.getByUsername(credentials.getUsername()) != null) {
            throw new ClientErrorException(409);
        }      
        List<BuildingBlock> blocks = new ArrayList<>(blockDAO.getAll());
        Client client = new Client(credentials, blocks);
        dao.create(client);
        return buildUri(uriInfo, client.getId());
    }

    public Client getById(String id, Principal principal) {
        Client client = dao.getById(id);
        requireResult(client, "Client not found");
        checkPermissions(client, userDAO.getByUsername(principal.getName()));
        return client;
    }

    public void update(Client client, Credentials credentials) {
        client.setUserName(credentials.getUsername());
        client.setPassword(credentials.getPassword());
        client.setApiKey(new APIKey());
        dao.update(client);
    }

    public void patch(Client client, JsonNode patchRequest) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonClient = mapper.valueToTree(client);
            JsonPatch patch = JsonPatch.fromJson(patchRequest);
            JsonNode patched = patch.apply(jsonClient);
            dao.update(mapper.treeToValue(patched, Client.class));
        } catch (JsonPatchException | IOException | IllegalArgumentException | NullPointerException ex) {
            throw new BadRequestException("Bad patch request");
        }
    }

    public void deleteById(ObjectId id) {
        Client client = dao.getById(id.toString());
        requireResult(client, "Client not found");
        dao.delete(client);
    }
}
