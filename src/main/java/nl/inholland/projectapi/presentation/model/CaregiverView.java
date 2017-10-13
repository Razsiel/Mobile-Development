package nl.inholland.projectapi.presentation.model;

import java.util.List;
import nl.inholland.projectapi.model.Role;

public class CaregiverView extends BaseView {

    public String id;
    public String name;
    public Role role;
    public List<MessageView> messages;
    public List<AppointmentView> appointments;
}
