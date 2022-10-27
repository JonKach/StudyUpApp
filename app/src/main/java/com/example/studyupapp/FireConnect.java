package com.example.studyupapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.studyupapp.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FireConnect {

    static DocumentReference registrar = FirebaseFirestore.getInstance().document("ChatRegistrar/chats");
    private static long mostRecentAccessCode;
    private static ArrayList<String> groupsLoader = new ArrayList<>();

    public static void createGroup(String groupName, Context context) { //creates access code, then joins the group
        registrar.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    mostRecentAccessCode = Objects.requireNonNull(documentSnapshot.getLong("currentChats")) + 1;
                }
                Map<String, Object> newAccessCode = new HashMap<>();
                newAccessCode.put("currentChats", mostRecentAccessCode);
                //updates firestore's number of chats
                registrar.set(newAccessCode);
                joinGroup(groupName + ":" + mostRecentAccessCode, context, true);
            }
        }); //if the same named group is created 2 times, they will have different access codes, making them different
    }

    public static void joinGroup(String groupId, Context context, boolean creating) {
        if(creating) {
            Data.appendData(groupId, context, Data.GROUPS_TXT, false);
            sendInitialMessage(groupId, MainActivity.userName + " created the group!", true, context);
        } else {
            FirebaseFirestore.getInstance().collection(groupId).document("MessageOrigin").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()) { //checks to see if the group exists using messageOrigin
                        if(newToGroup(groupId, context)) {
                            Data.appendData(groupId, context, Data.GROUPS_TXT, false);
                            sendInitialMessage(groupId, MainActivity.userName + " joined the group!", false, context);
                        } else {
                            Toast.makeText(context,"You are already in this group!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(context, "This group does not exist!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public static void sendInitialMessage(String destination, String message, boolean creatingGroup, Context context) {
        CollectionReference chat = FirebaseFirestore.getInstance().collection(destination);
        String messageIdentifier;
        Map<String, Object> messageInfo = new HashMap<>();
        messageInfo.put("Message", message);
        messageInfo.put("From", MainActivity.userName);
        messageInfo.put("Grade", MainActivity.userGrade);
        messageInfo.put("Timestamp", Timestamp.now());
        messageInfo.put("isReplying", false);
        messageInfo.put("replyMessage", "None");
        if(creatingGroup) messageIdentifier = "Origin"; //origin is the first message in group, "proof" it was created
        else messageIdentifier = String.valueOf(Math.random());
        //(above statement) RANDOM FOR NOW, CHANGE TO A CHAT ID SYSTEM LATER, FOR REPLIES
        chat.document("Message" + messageIdentifier).set(messageInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("Appd", "chat sent: " + message);
                if(creatingGroup) Toast.makeText(context, "You have created the group!", Toast.LENGTH_LONG).show();
                else Toast.makeText(context, "You have joined the group!", Toast.LENGTH_LONG).show();
                GroupsActivity.groupPos = 0; //sets the group pos to the newly joined/created group
                GroupsActivity.returnToHome(context);
            }
        });
    }

    public static void sendMessage(String destination, String message, String author, String grade, boolean isReplying, String replyMessage) {
        CollectionReference chat = FirebaseFirestore.getInstance().collection(destination);
        String messageIdentifier;
        Map<String, Object> messageInfo = new HashMap<>();
        messageInfo.put("Message", message);
        messageInfo.put("From", author);
        messageInfo.put("Grade", grade);
        messageInfo.put("Timestamp", Timestamp.now());
        messageInfo.put("isReplying", isReplying);
        messageInfo.put("replyMessage", replyMessage);
        messageIdentifier = String.valueOf(Math.random());
        //(above statement) RANDOM FOR NOW, CHANGE TO A CHAT ID SYSTEM LATER, FOR REPLIES
        chat.document("Message" + messageIdentifier).set(messageInfo); //sends message
    }


    private static boolean newToGroup(String groupId, Context context) {
        groupsLoader.clear();
        Data.loadData(context, groupsLoader, Data.GROUPS_TXT);
        for(String group: groupsLoader) {
            if(group.equals(groupId)) {
                return false;
            }
        }
        return true;
    }



}
