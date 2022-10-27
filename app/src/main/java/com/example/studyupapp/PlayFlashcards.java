package com.example.studyupapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlayFlashcards extends AppCompatActivity {

    ArrayList<String> flashcards = new ArrayList<>();

    TextView flashcardsSetName;
    TextView numberOfFlashcards;
    TextView flashcardText;
    FloatingActionButton nextFlashcardButton;
    FloatingActionButton prevFlashcardButton;
    CardView flashcardContainer;
    ImageButton returnToStudy;

    String flashcardSetFileName;
    int currentFlashcardPos;
    boolean showingFront;

    String prompt;
    String answer;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_flashcards);

        Intent i = getIntent();
        flashcardSetFileName = i.getStringExtra("FLASHCARDS_SET");
        Data.loadData(this, flashcards, flashcardSetFileName);


        flashcardsSetName = findViewById(R.id.playFlashcardsSetName);
        numberOfFlashcards = findViewById(R.id.numberOfFlashcards);
        flashcardText = findViewById(R.id.flashcardText);
        nextFlashcardButton = findViewById(R.id.nextFlashcardButton);
        prevFlashcardButton = findViewById(R.id.prevFlashcardButton);
        flashcardContainer = findViewById(R.id.flashcardContainer);
        returnToStudy = findViewById(R.id.returnFromPlayFlashcards);

        context = this;

        currentFlashcardPos = 0;
        showingFront = true;

        nextFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentFlashcardPos == flashcards.size() -1 ) {
                    setFlashcardInfo(0);
                } else {
                    setFlashcardInfo(currentFlashcardPos + 1);
                }
                showFlashcard(true);
            }
        });

        prevFlashcardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentFlashcardPos == 0) {
                    setFlashcardInfo(0);
                } else {
                    setFlashcardInfo(currentFlashcardPos - 1);
                }
                showFlashcard(true);
            }
        });

        flashcardContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFlashcard(!showingFront);
                showingFront = !showingFront;
            }
        });

        returnToStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra("NAME", MainActivity.userName); //preserves user name and grade
                i.putExtra("GRADE", MainActivity.userGrade);
                i.putExtra("DESTINATION_FRAGMENT", "STUDY");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        flashcardsSetName.setText(flashcardSetFileName.split("::")[0]); //sets the name of the flashcard set
        numberOfFlashcards.setText(flashcards.size() + " Flashcards");

        if(flashcards.isEmpty()) {
            abort(); //if there's no flashcards, no play
        } else {
            setFlashcardInfo(currentFlashcardPos);
            showFlashcard(showingFront);
        }
    }

    private void showFlashcard(boolean isFront) {
        if(isFront) {
            flashcardText.setText(prompt);
        } else {
            flashcardText.setText(answer);
        }
    }

    private void setFlashcardInfo(int flashcardPos) {
        String[] flashcardInfo = flashcards.get(flashcardPos).split("::");
        prompt = flashcardInfo[0];
        answer = flashcardInfo[1];
        currentFlashcardPos = flashcardPos;
    }

    private void abort() {
        Toast.makeText(this, "Blank Flashcard Set!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("NAME", MainActivity.userName); //preserves user name and grade
        i.putExtra("GRADE", MainActivity.userGrade);
        i.putExtra("DESTINATION_FRAGMENT", "STUDY");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}