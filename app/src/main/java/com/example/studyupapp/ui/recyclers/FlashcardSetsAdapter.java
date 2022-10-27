package com.example.studyupapp.ui.recyclers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyupapp.R;
import com.example.studyupapp.ui.study.StudyFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FlashcardSetsAdapter extends RecyclerView.Adapter<FlashcardSetsAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Context context;
    ArrayList<FlashcardSetsModel> flashcardSetsModels;

    public FlashcardSetsAdapter(Context context, ArrayList<FlashcardSetsModel> flashcardSetsModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.flashcardSetsModels = flashcardSetsModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public FlashcardSetsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflates layout, gives look to each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.flashcards_set_row, parent, false);
        return new FlashcardSetsAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardSetsAdapter.MyViewHolder holder, int position) {
        //assigning values to the views we created

        holder.setName.setText(flashcardSetsModels.get(position).getSetName());
    }

    @Override
    public int getItemCount() {
        //the recycler view just wants to know the number of items we want displayed
        return flashcardSetsModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView setName;
        ImageButton flashcardPlayButton;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            setName = itemView.findViewById(R.id.setNameDisplay);
            flashcardPlayButton = itemView.findViewById(R.id.playFlashcardButton);
            flashcardPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StudyFragment.playFlashcards(getAdapterPosition(), flashcardPlayButton);
                }
            });

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
