package com.example.studyupapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NotebookActivity extends AppCompatActivity {

    Spinner spinner;
    EditText notebookText;
    Button saveAndClose;
    private final String[] NOTEBOOKS = {"Personal", "School", "Other"};

    Context context;

    String selectedNotebook = "Personal"; //default
    boolean skipFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notebook);

        Intent i = getIntent();

        spinner = findViewById(R.id.notebookSpinner);
        notebookText = findViewById(R.id.notebookText);
        saveAndClose = findViewById(R.id.saveAndCloseNotebook);
        skipFirst = true;

        context = this;

        spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, NOTEBOOKS));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedItem = adapterView.getItemAtPosition(position).toString();
                if(!skipFirst) {
                    saveNotebook(false);
                } else {
                    skipFirst = false;
                }
                selectedNotebook = selectedItem;
                switch(selectedItem) {
                    case "Personal":
                        notebookText.setText(Data.loadString(context, Data.PERSONAL_NOTEBOOK_TXT));
                        break;
                    case "School":
                        notebookText.setText(Data.loadString(context, Data.SCHOOL_NOTEBOOK_TXT));
                        break;
                    case "Other":
                        notebookText.setText(Data.loadString(context, Data.OTHER_NOTEBOOK_TXT));
                        break;
                    default:
                        notebookText.setText(Data.loadString(context, Data.OTHER_NOTEBOOK_TXT));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveAndClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNotebook(true);
            }
        });
    }

    void saveNotebook(boolean closeActivity) {
        switch(selectedNotebook) {
            case "Personal":
                Data.saveData(new String[]{notebookText.getText().toString().replaceAll("\\n", "\n")}, false, context, Data.PERSONAL_NOTEBOOK_TXT);
                break;
            case "School":
                Data.saveData(new String[]{notebookText.getText().toString().replaceAll("\\n", "\n")}, false, context, Data.SCHOOL_NOTEBOOK_TXT);
                break;
            case "Other":
                Data.saveData(new String[]{notebookText.getText().toString().replaceAll("\\n", "\n")}, false, context, Data.OTHER_NOTEBOOK_TXT);
                break;
            default:
                Data.saveData(new String[]{notebookText.getText().toString().replaceAll("\\n", "\n")}, false, context, Data.PERSONAL_NOTEBOOK_TXT);
                break;
        }
        if(closeActivity) {
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("NAME", MainActivity.userName); //preserves user name and grade
            i.putExtra("GRADE", MainActivity.userGrade);
            i.putExtra("DESTINATION_FRAGMENT", "HOME");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
}