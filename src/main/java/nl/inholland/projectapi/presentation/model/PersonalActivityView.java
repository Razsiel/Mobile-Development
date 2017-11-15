package nl.inholland.projectapi.presentation.model;

import java.util.List;
import nl.inholland.projectapi.model.Status;

public class PersonalActivityView extends BaseView {

    public String name;
    public String description;
    public int points;
    public Status status;
    public String content;
    public String assignment;
    public List<LikeView> likes;
    public List<CommentView> comments;

}
