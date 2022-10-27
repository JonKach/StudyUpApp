package com.example.studyupapp.ui.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyupapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FlashcardsAdapter extends RecyclerView.Adapter<FlashcardsAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<FlashcardsModel> flashcardModels;

    public FlashcardsAdapter(Context context, ArrayList<FlashcardsModel> flashcardModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.flashcardModels = flashcardModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public FlashcardsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflates layout, gives look to each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.flashcards_row, parent, false);
        return new FlashcardsAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardsAdapter.MyViewHolder holder, int position) {
        //assigning values to the views we created

        holder.prompt.setText(flashcardModels.get(position).getPrompt());
        holder.answer.setText(flashcardModels.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        //the recycler view just wants to know the number of items we want displayed
        return flashcardModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView prompt;
        TextView answer;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            prompt = itemView.findViewById(R.id.promptDisplay);
            answer = itemView.findViewById(R.id.answerDisplay);

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
