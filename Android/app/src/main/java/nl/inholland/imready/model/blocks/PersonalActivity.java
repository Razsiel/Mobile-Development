package nl.inholland.imready.model.blocks;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.inholland.imready.model.NamedEntityModel;
import nl.inholland.imready.model.enums.BlockPartStatus;

public class PersonalActivity extends NamedEntityModel {
    public static final Creator<PersonalActivity> CREATOR = new Creator<PersonalActivity>() {
        @Override
        public PersonalActivity createFromParcel(Parcel in) {
            return new PersonalActivity(in);
        }

        @Override
        public PersonalActivity[] newArray(int size) {
            return new PersonalActivity[size];
        }
    };
    @SerializedName("Status")
    private BlockPartStatus status;
    @SerializedName("Content")
    private String content;
    @SerializedName("Deadline")
    private Date deadline;
    @SerializedName("Feedback")
    private List<Feedback> feedback;
    @SerializedName("Component")
    private PersonalComponent component;
    @SerializedName("Activity")
    private Activity activity;

    protected PersonalActivity(Parcel in) {
        super(in);
        status = BlockPartStatus.valueOf(in.readString());
        content = in.readString();
        // date
        feedback = new ArrayList<>();
        in.readTypedList(feedback, Feedback.CREATOR);
        component = in.readParcelable(Component.class.getClassLoader());
        activity = in.readParcelable(Activity.class.getClassLoader());
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public BlockPartStatus getStatus() {
        return status;
    }

    public void setStatus(BlockPartStatus status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public List<Feedback> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<Feedback> feedback) {
        this.feedback = feedback;
    }

    public PersonalComponent getComponent() {
        return component;
    }

    public void setComponent(PersonalComponent component) {
        this.component = component;
    }

    @Override
    public String getName() {
        return activity.getName();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(status.name());
        parcel.writeString(content);
        // date
        parcel.writeTypedList(feedback);
        parcel.writeParcelable(component, 0);
        parcel.writeParcelable(activity, 0);
    }
}
