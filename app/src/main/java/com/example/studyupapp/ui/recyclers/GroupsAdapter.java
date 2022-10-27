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

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<GroupModel> groupsModels;

    public GroupsAdapter(Context context, ArrayList<GroupModel> groupsModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.groupsModels = groupsModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public GroupsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflates layout, gives look to each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.groups_row, parent, false);
        return new GroupsAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsAdapter.MyViewHolder holder, int position) {
        //assigning values to the views we created

        holder.groupName.setText(groupsModels.get(position).getName());
    }

    @Override
    public int getItemCount() {
        //the recycler view just wants to know the number of items we want displayed
        return groupsModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            groupName = itemView.findViewById(R.id.reply_message_nameDisplay);

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
