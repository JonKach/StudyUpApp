package com.example.studyupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView nameBox;
    TextView gradeBox;
    String[] user_info;

    TextView heading;
    TextView subHeading;
    Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nameBox = findViewById(R.id.nameBox);
        gradeBox = findViewById(R.id.gradeBox);
        findViewById(R.id.invalidText).setVisibility(View.INVISIBLE);
        heading = findViewById(R.id.heading);
        subHeading = findViewById(R.id.subHeading);
        button = findViewById(R.id.registerButton);
        user_info = new String[2];
        Data.loadData(this, user_info, Data.USER_PROFILE_TXT);
        if(user_info[0] != null) nextActivity();
        else Log.d("Userd", "no data found");
    }

    public void register(View v) {
        if(nameBox.getText().toString().isEmpty() || gradeBox.getText().toString().isEmpty() || !isGradeValid(gradeBox)) {
            findViewById(R.id.invalidText).setVisibility(View.VISIBLE);
        } else {
            user_info[0] = nameBox.getText().toString();
            user_info[1] = gradeBox.getText().toString();
            Data.saveData(user_info, false, this, Data.USER_PROFILE_TXT);
            nextActivity();
        }
    }

    private boolean isGradeValid(TextView grade) {
        if((Integer.parseInt(grade.getText().toString())) >= 1 && (Integer.parseInt(grade.getText().toString())) <= 12){
            return true;
        }
        return false;
    }

    private void nextActivity() {
        Log.d("Userd", user_info[0] + " " + user_info[1]);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("NAME", user_info[0]);
        i.putExtra("GRADE", user_info[1]);
        i.putExtra("DESTINATION_FRAGMENT", "HOME");
        startActivity(i);
    }
}