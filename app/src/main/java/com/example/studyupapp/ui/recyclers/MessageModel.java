package com.example.studyupapp.ui.recyclers;

public class MessageModel {
    String name;
    String grade;
    String message;
    boolean isReply;
    String replyMessage;

    public MessageModel(String name, String grade, String message, boolean isReply, String replyMessage) {
        this.name = name;
        this.grade = grade;
        this.message = message;
        this.isReply = isReply;
        this.replyMessage = replyMessage;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public String getMessage() {
        return message;
    }

    public boolean isReplying() { return isReply; }

    public String getReplyMessage() {
        return replyMessage;
    }
}
