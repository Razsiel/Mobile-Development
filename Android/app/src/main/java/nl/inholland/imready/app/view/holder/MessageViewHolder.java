package nl.inholland.imready.app.view.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import nl.inholland.imready.R;
import nl.inholland.imready.app.view.listener.OnChangeListener;
import nl.inholland.imready.model.user.Message;

public class MessageViewHolder extends RecyclerView.ViewHolder implements FillableViewHolder<Message> {

    private final TextView messageView;

    public MessageViewHolder(View view) {
        super(view);
        messageView = view.findViewById(R.id.message);
    }

    @Override
    public void fill(@Nullable Context context, @NonNull Message data, @Nullable OnChangeListener<Message> changeListener) {
        messageView.setText(data.getMessage());
    }
}
