package com.example.studyupapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.studyupapp.ui.recyclers.GroupModel;
import com.example.studyupapp.ui.recyclers.GroupsAdapter;
import com.example.studyupapp.ui.recyclers.RecyclerViewInterface;
import com.example.studyupapp.ui.social.SocialFragment;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity implements RecyclerViewInterface {

    public static int groupPos;

    Button createGroupButton;
    Button joinGroupButton;
    CardView createCardView;
    CardView joinCardView;
    TextView myGroupsText;

    ImageButton socialCreateBack;
    TextView backText;

    TextView manageGroupHeader;

    //create group
    Button confirmCreationButton;
    TextView groupNameBox;
    CardView groupNameCard;

    //join group
    TextView accessCodeEnter;
    TextView groupNameEnter;
    Button confirmJoinGroupButton;
    CardView groupNameEnterCard;
    CardView accessCodeCard;

    Context context;

    GroupsAdapter adapter;
    RecyclerView groupsRecycler;
    LinearLayoutManager linearManager;
    public ArrayList<GroupModel> groupModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_intro);

        context = getApplicationContext();

        createGroupButton = findViewById(R.id.createGroupButton);
        joinGroupButton = findViewById(R.id.joinGroupButton);
        createCardView = findViewById(R.id.createGroupCard);
        joinCardView = findViewById(R.id.joinGroupCard);
        myGroupsText = findViewById(R.id.myGroupsTextBox);

        socialCreateBack = findViewById(R.id.social_create_back);
        backText = findViewById(R.id.back_text);

        //create group
        confirmCreationButton = findViewById(R.id.confirmCreationButton);
        groupNameBox = findViewById(R.id.groupNameBox);
        manageGroupHeader = findViewById(R.id.manageGroupHeader);
        groupNameCard = findViewById(R.id.groupNameCard);

        //join group
        accessCodeEnter = findViewById(R.id.accessCodeEnter);
        groupNameEnter = findViewById(R.id.groupNameEnter);
        confirmJoinGroupButton = findViewById(R.id.confirmJoinGroupButton);
        groupNameEnterCard = findViewById(R.id.groupEnterCard);
        accessCodeCard = findViewById(R.id.accessCodeCard);

        invisibleAll();

//        binding.socialLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.mint_green));
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createButton();
            }
        });

        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinButton();
            }
        });


        socialCreateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBack();
            }
        });

        for(String group : SocialFragment.socialGroupsList) {
            groupModels.add(new GroupModel(group.split(":")[0]));
        }

        groupsRecycler = findViewById(R.id.groupsRecycler);
        adapter = new GroupsAdapter(this, groupModels, this);
        groupsRecycler.setAdapter(adapter);
        linearManager = new LinearLayoutManager(this);
        groupsRecycler.setLayoutManager(linearManager);
        joinCardView.setVisibility(View.VISIBLE);
        joinGroupButton.setVisibility(View.VISIBLE);
    }

    private void invisibleAll() {
        socialCreateBack.setVisibility(View.INVISIBLE);
        backText.setVisibility(View.INVISIBLE);

        confirmCreationButton.setVisibility(View.INVISIBLE);
        groupNameBox.setVisibility(View.INVISIBLE);
        groupNameCard.setVisibility(View.INVISIBLE);

        manageGroupHeader.setVisibility(View.INVISIBLE);

        groupNameEnterCard.setVisibility(View.INVISIBLE);
        accessCodeCard.setVisibility(View.INVISIBLE);
        accessCodeEnter.setVisibility(View.INVISIBLE);
        groupNameEnter.setVisibility(View.INVISIBLE);
        confirmJoinGroupButton.setVisibility(View.INVISIBLE);
    }

    private void createButton() {
        createGroupButton.setVisibility(View.INVISIBLE);
        joinGroupButton.setVisibility(View.INVISIBLE);
        createCardView.setVisibility(View.INVISIBLE);
        joinCardView.setVisibility(View.INVISIBLE);
        groupsRecycler.setVisibility(View.INVISIBLE);
        myGroupsText.setVisibility(View.INVISIBLE);

        socialCreateBack.setVisibility(View.VISIBLE);
        backText.setVisibility(View.VISIBLE);

        manageGroupHeader.setVisibility(View.VISIBLE);
        manageGroupHeader.setText("Create Group");
        groupNameCard.setVisibility(View.VISIBLE);
        confirmCreationButton.setVisibility(View.VISIBLE);
        groupNameBox.setVisibility(View.VISIBLE);
    }

    private void createBack() {
        invisibleAll();

        createGroupButton.setVisibility(View.VISIBLE);
        joinGroupButton.setVisibility(View.VISIBLE);
        createCardView.setVisibility(View.VISIBLE);
        joinCardView.setVisibility(View.VISIBLE);
        groupsRecycler.setVisibility(View.VISIBLE);
        myGroupsText.setVisibility(View.VISIBLE);
    }

    private void joinButton() {
        createGroupButton.setVisibility(View.INVISIBLE);
        joinGroupButton.setVisibility(View.INVISIBLE);
        createCardView.setVisibility(View.INVISIBLE);
        joinCardView.setVisibility(View.INVISIBLE);
        groupsRecycler.setVisibility(View.INVISIBLE);
        myGroupsText.setVisibility(View.INVISIBLE);

        socialCreateBack.setVisibility(View.VISIBLE);
        backText.setVisibility(View.VISIBLE);

        manageGroupHeader.setVisibility(View.VISIBLE);
        manageGroupHeader.setText("Join Group");
        groupNameEnterCard.setVisibility(View.VISIBLE);
        accessCodeCard.setVisibility(View.VISIBLE);
        accessCodeEnter.setVisibility(View.VISIBLE);
        groupNameEnter.setVisibility(View.VISIBLE);
        confirmJoinGroupButton.setVisibility(View.VISIBLE);
    }

    public void confirmCreateGroup(View v) {
        String groupName = groupNameBox.getText().toString();
        FireConnect.createGroup(groupName, context);
        //since creating instantly joins you, make it so the access code is visible in the actual group, not creation page
    }

    public void confirmJoinGroup(View v) {
        String groupName = groupNameEnter.getText().toString();
        String accessCode = accessCodeEnter.getText().toString();
        FireConnect.joinGroup(groupName + ":" + accessCode, context, false);
    }

    public static void returnToHome(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("NAME", MainActivity.userName); //preserves user name and grade
        i.putExtra("GRADE", MainActivity.userGrade);
        i.putExtra("DESTINATION_FRAGMENT", "SOCIAL");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    public void onItemClick(int position) {
        groupPos = position;
        returnToHome(context);
    }
}