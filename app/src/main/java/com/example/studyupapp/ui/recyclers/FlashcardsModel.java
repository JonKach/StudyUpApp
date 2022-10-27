package com.example.studyupapp.ui.recyclers;

public class FlashcardsModel {
    String prompt;
    String answer;

    public FlashcardsModel(String prompt, String answer) {
        this.prompt = prompt;
        this.answer = answer;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getAnswer() {
        return answer;
    }
}
