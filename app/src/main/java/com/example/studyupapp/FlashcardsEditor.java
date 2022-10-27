package com.example.studyupapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studyupapp.ui.home.HomeFragment;
import com.example.studyupapp.ui.recyclers.FlashcardsAdapter;
import com.example.studyupapp.ui.recyclers.FlashcardsModel;
import com.example.studyupapp.ui.recyclers.RecyclerViewInterface;

import java.util.ArrayList;
import java.util.Objects;

public class FlashcardsEditor extends AppCompatActivity implements RecyclerViewInterface {

    String flashcardsFileName;
    ArrayList<String> loadedFlashcardInfo = new ArrayList<>();

    RecyclerView flashcardsRecycler;
    ArrayList<FlashcardsModel> flashcardModels = new ArrayList<>();
    FlashcardsAdapter adapter;
    LinearLayoutManager linearManager;

    TextView prompt;
    TextView answer;
    EditText flashcardsSetNameEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards_editor);

        Intent i = getIntent();
        flashcardsFileName = i.getStringExtra("FLASHCARDS_FILE_NAME");

        findViewById(R.id.addFlashcardButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFlashcard();
            }
        });

        findViewById(R.id.doneFlashcardsEditButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFlashcards();
            }
        });

        flashcardsSetNameEnter = findViewById(R.id.flashcardSetNameEnter);

        flashcardModels.clear();
        Data.loadData(this, loadedFlashcardInfo, flashcardsFileName);
        if(!loadedFlashcardInfo.isEmpty()) { //not brand new set
            flashcardsSetNameEnter.setText(flashcardsFileName.split("::")[0]);
            flashcardsSetNameEnter.setEnabled(false); //cannot change name after it's already been created
            for(String flashcard: loadedFlashcardInfo) {
                String[] flashcardInfo = flashcard.split("::");
                flashcardModels.add(new FlashcardsModel(flashcardInfo[0], flashcardInfo[1]));
            }
        }

        flashcardsRecycler = findViewById(R.id.flashcardsRecycler);
        adapter = new FlashcardsAdapter(this, flashcardModels, this);
        flashcardsRecycler.setAdapter(adapter);
        linearManager = new LinearLayoutManager(this);
        flashcardsRecycler.setLayoutManager(linearManager);

        prompt = findViewById(R.id.flashcard_prompt_enter);
        answer = findViewById(R.id.flashcard_answer_enter);

    }

    private void addFlashcard() {
        String modelPrompt = prompt.getText().toString();
        String modelAnswer = answer.getText().toString();
        prompt.setText("");
        answer.setText("");
        flashcardModels.add(new FlashcardsModel(modelPrompt, modelAnswer));
        adapter.notifyItemInserted(adapter.getItemCount()+1);
        flashcardsRecycler.smoothScrollToPosition(adapter.getItemCount());
    }

    private void saveFlashcards() {
        String[] flashcardData = new String[2];
        String setName;
        if(Objects.equals(flashcardsFileName, "New")) {
            setName = flashcardsSetNameEnter.getText().toString() + "::" + Math.random() + ".txt";
            Data.appendData(setName, this, Data.FLASHCARD_SETS_TXT, true);
        } else {
            setName = flashcardsFileName;
        }
        Data.saveData(true, this, setName); //clear flashcards already in file and then update them
        for(FlashcardsModel model: flashcardModels) {
            flashcardData[0] = model.getPrompt();
            flashcardData[1] = model.getAnswer();
            Data.appendData(flashcardData[0] + "::" + flashcardData[1], this, setName, true);
        }
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("NAME", MainActivity.userName); //preserves user name and grade
        i.putExtra("GRADE", MainActivity.userGrade);
        i.putExtra("DESTINATION_FRAGMENT", "STUDY");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onItemClick(int position) {
        flashcardModels.remove(position);
        adapter.notifyItemRemoved(position);
    }
}