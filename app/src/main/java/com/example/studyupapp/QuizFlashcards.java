package com.example.studyupapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyupapp.ui.study.StudyFragment;
import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class QuizFlashcards extends AppCompatActivity {

    String flashcardSetFileName;
    ArrayList<String> flashcards = new ArrayList<>();

    Toolbar quizHeader;
    TextView flashcardsSetName;
    TextView numberOfQuestions;
    TextView questionPrompt;
    Button choice1;
    Button choice2;
    Button choice3;
    EditText answerEnter;
    Button submitButton;
    ImageButton returnToStudy;
    TextView scoreDisplay;
    TextView quizFeedback;

    int currentCorrectAnswer;
    String currentCorrectString;
    Double numberOfCorrect;
    int numberOfTotalQuestions;

    Handler timeHandler = new Handler();
    Runnable quizHeaderBlue;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_flashcards);

        Intent i = getIntent();
        flashcardSetFileName = i.getStringExtra("FLASHCARDS_SET");
        Data.loadData(this, flashcards, flashcardSetFileName);

        quizHeader = findViewById(R.id.quizFlashcardsHeader);
        flashcardsSetName = findViewById(R.id.quizFlashcardsSetName);
        numberOfQuestions = findViewById(R.id.numberOfQuestions);
        questionPrompt = findViewById(R.id.questionPrompt);
        choice1 = findViewById(R.id.choiceOne);
        choice2 = findViewById(R.id.choiceTwo);
        choice3 = findViewById(R.id.choiceThree);
        answerEnter = findViewById(R.id.quizAnswerEnter);
        submitButton = findViewById(R.id.submitAnswerButton);
        returnToStudy = findViewById(R.id.returnFromQuizFlashcards);
        scoreDisplay = findViewById(R.id.quizScoreDisplay);
        quizFeedback = findViewById(R.id.quizFeedback);

        scoreDisplay.setVisibility(View.INVISIBLE);
        quizFeedback.setVisibility(View.INVISIBLE);

        context = this;

        quizHeader.setBackgroundColor(getResources().getColor(R.color.professional_blue));

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitWrittenAnswer();
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

        quizHeaderBlue = new Runnable() {
            @Override
            public void run() {
                quizHeader.setBackgroundColor(getResources().getColor(R.color.professional_blue));
            }
        };

        flashcardsSetName.setText(flashcardSetFileName.split("::")[0]);
        numberOfQuestions.setText(flashcards.size() + " Questions");
        numberOfTotalQuestions = flashcards.size();

        numberOfCorrect = 0.0;

        if(flashcards.isEmpty()) {
            abort(); //if there's no flashcards, no play
        } else {
            createQuestion();
        }
    }

    private void createQuestion() {
        if(flashcards.size() != 0) {
            int chosenQuestion = (int) (Math.random() * flashcards.size());
            //random generates 0-.99, we multiply by number of flashcards to get the number up to the number of flashcards, floor it to an integer by casting, and get that flashcard
            String[] flashcardInfo = flashcards.get(chosenQuestion).split("::");
            flashcards.remove(chosenQuestion); //assures that question won't pop up again
            questionPrompt.setText(flashcardInfo[0]); //sets the question to the prompt
            int multipleChoiceDecider = (int) (Math.random() * 2); //can equal 0 or 1
            Log.d("Appd", String.valueOf(multipleChoiceDecider));
            if (multipleChoiceDecider == 0 && flashcards.size() >= 3) {
                //multiple choice
                choice1.setVisibility(View.VISIBLE);
                choice2.setVisibility(View.VISIBLE);
                choice3.setVisibility(View.VISIBLE);
                answerEnter.setVisibility(View.INVISIBLE);
                submitButton.setVisibility(View.INVISIBLE);
                String alternative1 = flashcards.get((int) (Math.random() * flashcards.size())).split("::")[1]; //picks another random one as an alternative
                String alternative2 = flashcards.get((int) (Math.random() * flashcards.size())).split("::")[1]; //picks another random one as an alternative
                if (alternative1.equals(alternative2)) {
                    choice3.setVisibility(View.INVISIBLE); //if both our random alternative same, just have 2 options
                }
                switch ((int) (Math.random() * 3)) {
                    case 0:
                        choice1.setText(flashcardInfo[1]); //sets the correct answer randomly to one of the buttons
                        currentCorrectAnswer = 1;
                        choice2.setText(alternative1);
                        choice3.setText(alternative2);
                        break;
                    case 1:
                        choice2.setText(flashcardInfo[1]);
                        currentCorrectAnswer = 2;
                        choice1.setText(alternative1);
                        choice3.setText(alternative2);
                        break;
                    case 2:
                        choice3.setText(flashcardInfo[1]);
                        currentCorrectAnswer = 3;
                        choice1.setText(alternative1);
                        choice2.setText(alternative2);
                        break;
                }
            } else {
                //answer enter
                answerEnter.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.VISIBLE);
                choice1.setVisibility(View.INVISIBLE);
                choice2.setVisibility(View.INVISIBLE);
                choice3.setVisibility(View.INVISIBLE);
                currentCorrectString = flashcardInfo[1];
            }
        } else {
            Toast.makeText(this, "Done!", Toast.LENGTH_LONG).show();
            questionPrompt.setVisibility(View.INVISIBLE);
            answerEnter.setVisibility(View.INVISIBLE);
            submitButton.setVisibility(View.INVISIBLE);
            choice1.setVisibility(View.INVISIBLE);
            choice2.setVisibility(View.INVISIBLE);
            choice3.setVisibility(View.INVISIBLE);
            scoreDisplay.setText((int) ((numberOfCorrect / numberOfTotalQuestions)*100) + "%");
            if((int) ((numberOfCorrect / numberOfTotalQuestions)*100) > 90) {
                quizFeedback.setText("Great Job!");
            } else {
                quizFeedback.setText("Keep Trying!");
            }
            scoreDisplay.setVisibility(View.VISIBLE);
            quizFeedback.setVisibility(View.VISIBLE);
        }
    }

    public void multiple_choice_click(View v) { //click listener
        switch(v.getId()) {
            case R.id.choiceOne:
                if(currentCorrectAnswer == 1) {
                    correct();
                } else {
                    wrong();
                }
                break;
            case R.id.choiceTwo:
                if(currentCorrectAnswer == 2) {
                    correct();
                } else {
                    wrong();
                }
                break;
            case R.id.choiceThree:
                if(currentCorrectAnswer == 3) {
                    correct();
                } else {
                    wrong();
                }
                break;
            default:
                wrong();
                break;
        }
    }

    private void submitWrittenAnswer() {
        if(answerEnter.getText().toString().equals(currentCorrectString)) {
            correct();
        } else {
            wrong();
        }
        answerEnter.setText("");
    }

    private void correct() {
        numberOfCorrect += 1.0;
        quizHeader.setBackgroundColor(getResources().getColor(R.color.mint_green));
        timeHandler.postDelayed(quizHeaderBlue, 500);
        Toast.makeText(this, "Correct!", Toast.LENGTH_LONG).show();
        createQuestion();
    }

    private void wrong() {
        quizHeader.setBackgroundColor(getResources().getColor(R.color.soft_red));
        timeHandler.postDelayed(quizHeaderBlue, 500);
        Toast.makeText(this, "Wrong!", Toast.LENGTH_LONG).show();
        createQuestion();
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