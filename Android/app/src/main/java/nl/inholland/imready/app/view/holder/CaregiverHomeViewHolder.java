package nl.inholland.imready.app.view.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import nl.inholland.imready.R;
import nl.inholland.imready.app.view.listener.OnChangeListener;
import nl.inholland.imready.service.model.ClientsResponse;

/**
 * Created by Peter on 17/12/2017.
 */

public class CaregiverHomeViewHolder implements FillableViewHolder<ClientsResponse> {
    private final TextView nameView;
    private final TextView notificationCountView;

    public CaregiverHomeViewHolder(View view){
        this.nameView = view.findViewById(R.id.clientName);
        this.notificationCountView = view.findViewById(R.id.clientNotifications);
    }


    @Override
    public void fill(@NonNull Context context, @NonNull ClientsResponse data, @Nullable OnChangeListener<ClientsResponse> changeListener) {
        nameView.setText(data.getName());
        notificationCountView.setText(data.getNotificationCount());
    }
}
