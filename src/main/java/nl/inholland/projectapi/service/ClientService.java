package nl.inholland.projectapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import nl.inholland.projectapi.model.Client;
import nl.inholland.projectapi.persistence.ClientDAO;

public class ClientService extends BaseService {

    private final ClientDAO dao;

    @Inject
    public ClientService(ClientDAO dao) {
        this.dao = dao;
    }

    public List<Client> getAll() {
        List<Client> clients = dao.getAllClients();
        if (clients.isEmpty()) {
            throw new NotFoundException("Not found");
        }
        return clients;
    }
    public void create(Client client) {
        dao.create(client);
    }
    public Client getById(String id) {
        Client client = dao.get(id);
        if (client == null) {
            throw new NotFoundException("Not found");
        }
        return client;
    }
    public void patch(String userId, JsonNode patchRequest) {
        JsonNode patched = null;
        try {
            Client client = getById(userId);//Get client to be patched from userId
            ObjectMapper mapper = new ObjectMapper();//Map client to json
            JsonNode jsonClient = mapper.valueToTree(client);//map client to json
            JsonPatch patch = JsonPatch.fromJson(patchRequest);//Get patchrequest from body
            patched = patch.apply(jsonClient);//Patch client        
            dao.update(mapper.treeToValue(patched, Client.class));//Send patch to database as Client object
            
        } catch (JsonPatchException | IOException | IllegalArgumentException ex) {
            ex.printStackTrace();
            throw new BadRequestException("Bad patch request");
        }
    }
}
