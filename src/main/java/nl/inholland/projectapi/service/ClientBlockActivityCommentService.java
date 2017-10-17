package nl.inholland.projectapi.service;

import java.net.URI;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import nl.inholland.projectapi.model.Activity;
import nl.inholland.projectapi.model.BuildingBlock;
import nl.inholland.projectapi.model.Client;
import nl.inholland.projectapi.model.Comment;
import nl.inholland.projectapi.persistence.ClientDAO;
import org.bson.types.ObjectId;

public class ClientBlockActivityCommentService extends BaseService{
    
    private final ClientDAO clientDAO;

    @Inject
    public ClientBlockActivityCommentService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }
    
    public List<Comment> getAll(Client client, String blockId, String activityId) {
        for (BuildingBlock b : client.getBuildingBlocks()) {
            if (b.getId().equals(blockId)) {
                for (Activity a : b.getActivities()) {
                    if (a.getId().equals(activityId)) {
                        return a.getComments();
                    }
                }
            }
        }
        throw new NotFoundException("Comments not found");
    }

    public URI create(Client client, String blockId, String activityId, Comment comment, UriInfo uriInfo) {
        comment.setId(new ObjectId());
        comment.setDateTime(new Date());
        try {
            comment.getSenderId();
            comment.getMessage();
        } catch (Exception e) {
            throw new BadRequestException("Invalid Comment object");
        }

        getAll(client, blockId, activityId).add(comment);
        clientDAO.update(client);
        return buildUri(uriInfo, comment.getId());
    }

    public Comment get(Client client, String blockId, String activityId, String commentId) {
        for (Comment c : getAll(client, blockId, activityId)) {
            if (c.getId().equals(commentId)) {
                return c;
            }
        }
        throw new NotFoundException("Comment not found");
    }

    public void delete(Client client, String blockId, String activityId, String commentId) {
        getAll(client, blockId, activityId).removeIf(i -> i.getId().equals(commentId));
        clientDAO.update(client);
    }    
}
