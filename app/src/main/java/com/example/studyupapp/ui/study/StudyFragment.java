package com.example.studyupapp.ui.study;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyupapp.Data;
import com.example.studyupapp.FlashcardsEditor;
import com.example.studyupapp.LoginActivity;
import com.example.studyupapp.PlayFlashcards;
import com.example.studyupapp.QuizFlashcards;
import com.example.studyupapp.R;
import com.example.studyupapp.databinding.FragmentStudyBinding;
import com.example.studyupapp.ui.recyclers.FlashcardSetsAdapter;
import com.example.studyupapp.ui.recyclers.FlashcardSetsModel;
import com.example.studyupapp.ui.recyclers.RecyclerViewInterface;

import java.util.ArrayList;

public class StudyFragment extends Fragment implements RecyclerViewInterface {

    private FragmentStudyBinding binding;
    public static Context studyFragmentContext;

    static ArrayList<String> flashcardSets = new ArrayList<>();
    RecyclerView flashcardSetsRecycler;
    FlashcardSetsAdapter adapter;
    ArrayList<FlashcardSetsModel> flashcardSetsModels = new ArrayList<>();
    LinearLayoutManager linearManager;

    static int currentlySelectedSetPosition; //the position in the recycler of the view that "play" button is clicked on

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStudyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        studyFragmentContext = getActivity();

        binding.createFlashcardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFlashcards();
            }
        });

        Data.loadData(studyFragmentContext, flashcardSets, Data.FLASHCARD_SETS_TXT);

        if(!flashcardSets.isEmpty()) {
            setUpSets();
        }

        return root;
    }

    public void createFlashcards() { //OnClick Listener
        Intent i = new Intent(studyFragmentContext, FlashcardsEditor.class);
        i.putExtra("FLASHCARDS_FILE_NAME", "New");
        startActivity(i);
    }

    private void setUpSets() {
        for(String set: flashcardSets) {
            flashcardSetsModels.add(new FlashcardSetsModel(set.split("::")[0])); //sets name of flashcard set to model
        }
        flashcardSetsRecycler = binding.flashcardsSetsRecycler;
        adapter = new FlashcardSetsAdapter(studyFragmentContext, flashcardSetsModels, this);
        flashcardSetsRecycler.setAdapter(adapter);
        linearManager = new LinearLayoutManager(studyFragmentContext);
        flashcardSetsRecycler.setLayoutManager(linearManager);
    }

    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(studyFragmentContext, FlashcardsEditor.class);
        i.putExtra("FLASHCARDS_FILE_NAME", flashcardSets.get(position));
        startActivity(i);
    }

    public static void playFlashcards(int positionOfSet, View view) {
//        currentlySelectedSetPosition = positionOfSet;
        PopupMenu popup = new PopupMenu(studyFragmentContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.flashcard_actions, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.learn:
                        Intent i = new Intent(studyFragmentContext, PlayFlashcards.class);
                        i.putExtra("FLASHCARDS_SET", flashcardSets.get(positionOfSet));
                        studyFragmentContext.startActivity(i);
                        return true;
                    case R.id.quiz:
                        Intent j = new Intent(studyFragmentContext, QuizFlashcards.class);
                        j.putExtra("FLASHCARDS_SET", flashcardSets.get(positionOfSet));
                        studyFragmentContext.startActivity(j);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}