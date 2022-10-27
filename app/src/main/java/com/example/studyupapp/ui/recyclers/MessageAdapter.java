package com.example.studyupapp.ui.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyupapp.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter {
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<MessageModel> messageModels;

    public MessageAdapter(Context context, ArrayList<MessageModel> messageModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.messageModels = messageModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).isReplying()) {
            return 2;
        } else return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflates layout, gives look to each row
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if(viewType == 1) {
            view = inflater.inflate(R.layout.messages_row, parent, false);
            return new ViewHolderOne(view, recyclerViewInterface);
        } else {
            view = inflater.inflate(R.layout.reply_messages_row, parent, false);
            return new ViewHolderTwo(view, recyclerViewInterface);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //assigning values to the views we created

        if(messageModels.get(position).isReplying()) {
            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
            viewHolderTwo.replyName.setText(messageModels.get(position).getName());
            viewHolderTwo.replyGrade.setText(messageModels.get(position).getGrade());
            viewHolderTwo.replyMessage.setText(messageModels.get(position).getMessage());
            viewHolderTwo.replyToMessage.setText("Reply: " + messageModels.get(position).getReplyMessage());
        } else {
            ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
            viewHolderOne.name.setText(messageModels.get(position).getName());
            viewHolderOne.grade.setText(messageModels.get(position).getGrade());
            viewHolderOne.message.setText(messageModels.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        //the recycler view just wants to know the number of items we want displayed
        return messageModels.size();
    }

    public static class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView name, grade, message;

        public ViewHolderOne(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            name = itemView.findViewById(R.id.message_nameDisplay);
            grade = itemView.findViewById(R.id.message_gradeDisplay);
            message = itemView.findViewById(R.id.message_messageDisplay);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

    public static class ViewHolderTwo extends RecyclerView.ViewHolder {

        TextView replyName, replyGrade, replyMessage, replyToMessage;

        public ViewHolderTwo(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            replyName = itemView.findViewById(R.id.reply_message_nameDisplay);
            replyGrade = itemView.findViewById(R.id.reply_message_gradeDisplay);
            replyMessage = itemView.findViewById(R.id.reply_message_messageDisplay);
            replyToMessage = itemView.findViewById(R.id.reply_replyToDisplay);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }


}
