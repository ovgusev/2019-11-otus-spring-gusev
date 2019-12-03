package com.ovgusev.domain;

public class Answer {
    private String question;
    private String answer;
    private Boolean isCorrect;

    public Answer(String question, String answer, Boolean isCorrect) {
        this.question = question;
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }
}
