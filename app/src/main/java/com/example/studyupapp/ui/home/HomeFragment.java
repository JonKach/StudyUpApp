package com.example.studyupapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.studyupapp.Data;
import com.example.studyupapp.LoginActivity;
import com.example.studyupapp.MainActivity;
import com.example.studyupapp.NotebookActivity;
import com.example.studyupapp.R;
import com.example.studyupapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ArrayList<String> setsLoader = new ArrayList<>();
    private ArrayList<String> groupsLoader = new ArrayList<>();
    String[] notes;

    TextView numberOfSets;
    TextView numberOfGroups;
    EditText notesEnter;
    EditText reminderDate;
    EditText reminderText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        Context context = getActivity();
        binding.nameDisplay.setText(MainActivity.userName);
        String prefix;
        switch(MainActivity.userGrade) {
            case "1":
                prefix = "1st";
                break;
            case "2":
                prefix = "2nd";
                break;
            case "3":
                prefix = "3rd";
                break;
            default:
                prefix = MainActivity.userGrade + "th";
        }
        binding.gradeDisplay.setText(prefix + " Grade");

        binding.developerSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.actions, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.unregister:
                                Data.saveData(true, context, Data.USER_PROFILE_TXT);
                                Data.saveData(true, context, Data.GROUPS_TXT);
                                Data.loadData(context, setsLoader, Data.FLASHCARD_SETS_TXT);
                                for(String setName : setsLoader) {
                                    Data.saveData(true, context, setName);
                                }
                                Data.saveData(true, context, Data.FLASHCARD_SETS_TXT);
                                Data.saveData(true, context, Data.NOTES_TXT);
                                Data.saveData(true, context, Data.PERSONAL_NOTEBOOK_TXT);
                                Data.saveData(true, context, Data.SCHOOL_NOTEBOOK_TXT);
                                Data.saveData(true, context, Data.OTHER_NOTEBOOK_TXT);
                                Data.saveData(true, context, Data.REMINDERS_TXT);
                                Intent i = new Intent(context, LoginActivity.class);
                                startActivity(i);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });

        binding.notebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NotebookActivity.class);
                startActivity(i);
            }
        });

        numberOfSets = binding.numberOfSetsText;
        numberOfGroups = binding.numberOfGroupsText;
        notesEnter = binding.notesEnter;
        reminderDate = binding.reminderDate;
        reminderText = binding.reminderText;
        reminderDate.setVisibility(View.INVISIBLE);

        Data.loadData(context, setsLoader, Data.FLASHCARD_SETS_TXT);
        Data.loadData(context, groupsLoader, Data.GROUPS_TXT);
        if(setsLoader.size() == 1) {
            numberOfSets.setText(setsLoader.size() + " Flashcard Set");
        } else {
            numberOfSets.setText(setsLoader.size() + " Flashcard Sets");
        }
        if(groupsLoader.size() == 1) {
            numberOfGroups.setText(groupsLoader.size() + " Group");
        } else {
            numberOfGroups.setText(groupsLoader.size() + " Groups");
        }

        try {
            notesEnter.setText(Data.loadString(context, Data.NOTES_TXT));
        } catch(NullPointerException npe) {
            npe.printStackTrace();
        }

        try{
            String reminderString;
            reminderString = Data.loadString(context, Data.REMINDERS_TXT);
            String[] reminderInfo = reminderString.split("::");
            if(reminderInfo.length > 1) { //if loadString returned default "" then don't do this cause array will be out of bounds
                reminderText.setText(reminderInfo[0]);
                reminderDate.setText(reminderInfo[1]);
                reminderDate.setVisibility(View.VISIBLE);
            } else {
                reminderDate.setVisibility(View.INVISIBLE);
            }
        } catch(NullPointerException npe) {
            npe.printStackTrace();
        }

        binding.addReminderDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminderDate.setVisibility(View.VISIBLE);
            }
        });

        binding.clearReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminderDate.setVisibility(View.INVISIBLE);
                reminderText.setText("");
                reminderDate.setText("");
            }
        });
        return root;
    }


    @Override
    public void onDestroyView() {
        Data.saveData(new String[]{notesEnter.getText().toString().replaceAll("\\n", "\n")}, false, getActivity(), Data.NOTES_TXT);
        Data.saveData(new String[]{reminderText.getText().toString() + "::" + reminderDate.getText().toString()}, false, getActivity(), Data.REMINDERS_TXT);
        super.onDestroyView();
        binding = null;
    }


}