package com.FinalP.finalchat.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.FinalP.finalchat.R;
import com.FinalP.finalchat.listeners.SimpleListener;
import com.FinalP.finalchat.models.application.User;
import com.FinalP.finalchat.services.Callback;
import com.FinalP.finalchat.services.DatabaseService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.ExecutionException;


public class LastMessagesAdapter extends FirebaseRecyclerAdapter<String, LastMessagesAdapter.UserViewHolder> {
    SimpleListener<String> openChat;
    static String currentEmail;
    static String currentUserEmail;
    public LastMessagesAdapter(@NonNull FirebaseRecyclerOptions<String> options, SimpleListener<String> openChat) {
        super(options);
        this.openChat = openChat;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull String model) {
        try {
            currentEmail=model;
            if (model==holder.emailView.getText().toString()){}
            holder.bind(model, openChat);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_row, parent, false);
        return new UserViewHolder(view);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView emailView;
        ImageView avatar;
        ConstraintLayout rootLayout;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.textViewName);
            emailView = itemView.findViewById(R.id.textViewEmail);
            avatar=itemView.findViewById(R.id.imageViewAvatar);
            rootLayout = itemView.findViewById(R.id.userLayoutId);
            currentUserEmail=DatabaseService.reformString(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        }

        public void bind(String key, SimpleListener<String> openChat) throws InterruptedException, ExecutionException {
                DatabaseService.getNameFromKey(key, new Callback<User>() {
                    @Override
                    public void call(User arg) {
                        if (arg.id.equals("technicaccount")){
                            avatar.setImageResource(R.drawable.fav);
                            nameView.setText("Избранное");
                            emailView.setText("");
                            emailView.setHeight(0);
                            emailView.setWidth(0);
                        }
                        else {
                            nameView.setText(arg.name);
                            if (!arg.avatar.equals("Default")) {
                                avatar.setImageBitmap(DatabaseService.StringToBitMap(arg.avatar));
                            } else {
                                avatar.setImageResource(R.drawable.alien_without_text);
                            }
                        }
                    }
                });
                emailView.setText(key);

            rootLayout.setOnClickListener(v -> {
                openChat.onValue(DatabaseService.reformString(key));
            });
        }
    }
}
