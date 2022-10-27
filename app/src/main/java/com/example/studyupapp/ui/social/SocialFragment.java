package com.example.studyupapp.ui.social;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.studyupapp.Data;
import com.example.studyupapp.FireConnect;
import com.example.studyupapp.GroupsActivity;
import com.example.studyupapp.MainActivity;
import com.example.studyupapp.R;
import com.example.studyupapp.databinding.FragmentSocialBinding;
import com.example.studyupapp.ui.recyclers.MessageAdapter;
import com.example.studyupapp.ui.recyclers.MessageModel;
import com.example.studyupapp.ui.recyclers.RecyclerViewInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SocialFragment extends Fragment implements RecyclerViewInterface {

    private FragmentSocialBinding binding;
    private Context context;
    public static ArrayList<String> socialGroupsList = new ArrayList<>();
    public ArrayList<MessageModel> messageModels = new ArrayList<>();
    private int groupPosition;

    ListenerRegistration realtimeListener;

    boolean skipFirstRealtime;
    boolean skipFirstGradeFilter;

    MessageAdapter adapter;
    RecyclerView messageRecycler;
    LinearLayoutManager linearManager;

    Spinner spinner;
    int gradeFilterLow;
    int gradeFilterHigh;

    private final String[] GRADE_FILTER = {"All Grades", "High School", "Middle School", "Elementary School", "Your Grade"};

    public boolean isReplying;
    private String currentReplyMessage;

    Handler timeHandler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSocialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        binding.goToGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGroups();
            }
        });

        binding.otherGroupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDestroyView(); //detatch the current groups realtime listener
                goToGroups();
            }
        });

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        binding.cancelReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isReplying = false;
                currentReplyMessage = "";
                binding.cancelReplyButton.setVisibility(View.INVISIBLE);
                binding.replyText.setVisibility(View.INVISIBLE);
                binding.replyToolbar.setVisibility(View.INVISIBLE);
            }
        });

        context = getActivity();
        messageModels.clear();

        messageRecycler = binding.messagesRecycler;
        adapter = new MessageAdapter(this.getContext(), messageModels, this);
        messageRecycler.setAdapter(adapter);
        linearManager = new LinearLayoutManager(this.getContext());
        messageRecycler.setLayoutManager(linearManager);

        Data.loadData(context, socialGroupsList, Data.GROUPS_TXT);
        groupPosition = GroupsActivity.groupPos;

        spinner = binding.gradeFilter;
        spinner.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, GRADE_FILTER));


        gradeFilterLow = 1;
        gradeFilterHigh = 12;

        isReplying = false;

        setupGroup(groupPosition);

        return root;
    }

    private void setupGroup(int positionOfGroup) {
        if(!socialGroupsList.isEmpty()) { //user has at least one group
            binding.socialHeader.setVisibility(View.VISIBLE);
            binding.socialSubheader.setVisibility(View.VISIBLE);
            binding.socialGroupName.setVisibility(View.VISIBLE);
            binding.socialGradeFilterText.setVisibility(View.VISIBLE);
            binding.gradeFilter.setVisibility(View.VISIBLE);
            binding.otherGroupsButton.setVisibility(View.VISIBLE);
            binding.socialAccessCode.setVisibility(View.VISIBLE);
            binding.textBoxBackground.setVisibility(View.VISIBLE);
            binding.textBox.setVisibility(View.VISIBLE);
            binding.sendButton.setVisibility(View.VISIBLE);
            binding.messagesRecycler.setVisibility(View.VISIBLE);
            binding.replyText.setVisibility(View.INVISIBLE);
            binding.replyToolbar.setVisibility(View.INVISIBLE);
            binding.cancelReplyButton.setVisibility(View.INVISIBLE);
            binding.noGroupsWarning.setVisibility(View.INVISIBLE);
            binding.goToGroups.setVisibility(View.INVISIBLE);
            skipFirstRealtime = true;
            skipFirstGradeFilter = true;

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    String selectedItem = adapterView.getItemAtPosition(position).toString();
                    Log.d("Appd", "selected item: " + selectedItem);
                    switch(selectedItem) {
                        case "All Grades":
                            filterGrades(1, 12);
                            binding.socialGradeFilterText.setText("Currently Viewing Messages from All Grades");
                            break;
                        case "High School":
                            filterGrades(9, 12);
                            binding.socialGradeFilterText.setText("Currently Viewing Messages from 9th-12th Grade");
                            break;
                        case "Middle School":
                            filterGrades(6, 8);
                            binding.socialGradeFilterText.setText("Currently Viewing Messages from 6th-8th Grade");
                            break;
                        case "Elementary School":
                            filterGrades(1, 5);
                            binding.socialGradeFilterText.setText("Currently Viewing Messages from 1st-5th Grade");
                            break;
                        case "Your Grade":
                            filterGrades(Integer.parseInt(MainActivity.userGrade), Integer.parseInt(MainActivity.userGrade));
                            binding.socialGradeFilterText.setText("Currently Viewing Messages from Your Grade");
                            break;
                        default:
                            filterGrades(1, 12);
                            binding.socialGradeFilterText.setText("Currently Viewing Messages from All Grades");
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            loadGroup(positionOfGroup); //loads the most recently joined group, top one on data file
            queryRecentMessages(socialGroupsList.get(positionOfGroup), false);
//            startRealtimeListener(socialGroupsList.get(positionOfGroup));
        } else {
            binding.socialHeader.setVisibility(View.INVISIBLE);
            binding.socialSubheader.setVisibility(View.INVISIBLE);
            binding.socialGroupName.setVisibility(View.INVISIBLE);
            binding.socialGradeFilterText.setVisibility(View.INVISIBLE);
            binding.gradeFilter.setVisibility(View.INVISIBLE);
            binding.otherGroupsButton.setVisibility(View.INVISIBLE);
            binding.socialAccessCode.setVisibility(View.INVISIBLE);
            binding.textBoxBackground.setVisibility(View.INVISIBLE);
            binding.textBox.setVisibility(View.INVISIBLE);
            binding.sendButton.setVisibility(View.INVISIBLE);
            binding.messagesRecycler.setVisibility(View.INVISIBLE);
            binding.replyText.setVisibility(View.INVISIBLE);
            binding.replyToolbar.setVisibility(View.INVISIBLE);
            binding.cancelReplyButton.setVisibility(View.INVISIBLE);
            binding.noGroupsWarning.setVisibility(View.VISIBLE);
            binding.goToGroups.setVisibility(View.VISIBLE);
        }
    }

    private void goToGroups() {
        startActivity(new Intent(getActivity(), GroupsActivity.class));
    }

    private void loadGroup(int positionOfGroup) { //use this when loading any group from group switcher
        String[] group = socialGroupsList.get(positionOfGroup).split(":");
        binding.socialGroupName.setText(group[0]);
        binding.socialAccessCode.setText("Access Code: " + group[1]);
    }

    private void sendMessage() {
        if(isReplying) {
            FireConnect.sendMessage(socialGroupsList.get(groupPosition), binding.textBox.getText().toString(), MainActivity.userName, MainActivity.userGrade, true, currentReplyMessage);
            isReplying = false;
            binding.replyToolbar.setVisibility(View.INVISIBLE);
            binding.replyText.setVisibility(View.INVISIBLE);
            binding.cancelReplyButton.setVisibility(View.INVISIBLE);
        } else {
            FireConnect.sendMessage(socialGroupsList.get(groupPosition), binding.textBox.getText().toString(), MainActivity.userName, MainActivity.userGrade, false, "None");
        }
        binding.textBox.setText("");
    }

    private void queryRecentMessages(String destination, boolean isFilter) {
        CollectionReference chat = FirebaseFirestore.getInstance().collection(destination);
        chat.orderBy("Timestamp", Query.Direction.DESCENDING).limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                for(int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    if(inRange(gradeFilterLow, gradeFilterHigh, Integer.parseInt(String.valueOf(docs.get(queryDocumentSnapshots.size() - 1 - i).get("Grade"))))) {
                        setUpMessageModel(docs.get(queryDocumentSnapshots.size()-1-i));//iterating backwards through the descending sort
                        Log.d("Appd", "From Query, Message: " + docs.get(queryDocumentSnapshots.size()-1-i).getString("Message") + " Author: " + docs.get(queryDocumentSnapshots.size()-1-i).getString("From"));
                    }
                }
                if(!isFilter) startRealtimeListener(socialGroupsList.get(groupPosition));
                Runnable scrollDown = new Runnable() {
                    @Override
                    public void run() {
                        messageRecycler.smoothScrollToPosition(adapter.getItemCount());
                        Log.d("Appd", "posted");
                    }
                };
                timeHandler.postDelayed(scrollDown, 1500);
            }
        });
    }

    private void startRealtimeListener(String destination) {
        CollectionReference chat = FirebaseFirestore.getInstance().collection(destination);
        realtimeListener = chat.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
              if(value != null) {
                  if(!skipFirstRealtime) {
                      chat.orderBy("Timestamp", Query.Direction.DESCENDING).limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                          @Override
                          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                              for(DocumentSnapshot doc : queryDocumentSnapshots) {
                                  int iteration = 1;
                                  if(inRange(gradeFilterLow, gradeFilterHigh, Integer.parseInt(String.valueOf(doc.get("Grade"))))){
                                      setUpMessageModel(doc);
                                      Log.d("Appd", "iteration: " + iteration + " " + destination + ", From realtime, Message: " + doc.getString("Message") + " Author: " + doc.getString("From"));
                                  }
                              }
                          }
                      });
                  } else {
                      skipFirstRealtime = false;
                  }
              }
            }
        });
    }

    private void setUpMessageModel(DocumentSnapshot doc) {
        String name = doc.getString("From");
        String suffix;
        String grade = doc.getString("Grade");
        String message = doc.getString("Message");
        boolean isReply = doc.getBoolean("isReplying");
        String replyMessage = doc.getString("replyMessage");

        switch(Objects.requireNonNull(grade)) {
            case "1":
                suffix = "st";
                break;
            case "2":
                suffix = "nd";
                break;
            case "3":
                suffix = "rd";
                break;
            default:
                suffix = "th";
        }
        grade += (suffix + " grade");

        messageModels.add(new MessageModel(name, grade, message, isReply, replyMessage));
        if(messageModels.size() > 10)  {
            messageModels.remove(0);
            adapter.notifyItemRemoved(0);
        }
        adapter.notifyItemRangeChanged(messageModels.size()-2, messageModels.size());
        messageRecycler.smoothScrollToPosition(adapter.getItemCount());
    }


    @Override
    public void onItemClick(int position) {
        //reply window or activity functionality on click of message
        isReplying = true;
        currentReplyMessage = messageModels.get(position).getMessage();
        binding.replyText.setText("Replying to " + messageModels.get(position).getName());
        binding.replyText.setVisibility(View.VISIBLE);
        binding.replyToolbar.setVisibility(View.VISIBLE);
        binding.cancelReplyButton.setVisibility(View.VISIBLE);
    }

    private void filterGrades(int low, int high) {
        if(!skipFirstGradeFilter) {
            Log.d("Appd", "filter grades");
            messageModels.clear();
            adapter.notifyDataSetChanged();
            gradeFilterLow = low;
            gradeFilterHigh = high;
            queryRecentMessages(socialGroupsList.get(groupPosition), true);
        } else {
            Log.d("Appd", "skipping filter");
        }
        skipFirstGradeFilter = false;
    }

    private boolean inRange(int low, int high, int value) {
        return value >= low && value <= high;
    }


    @Override
    public void onDestroyView() {
        Log.d("Appd", "destroyed");
        if(realtimeListener != null) realtimeListener.remove();
        super.onDestroyView();
        binding = null;
    }
}