package com.ovgusev.domain;

public class Answer {
    private Question question;
    private String answer;
    private Boolean correct;

    public Answer(Question question, String answer, Boolean correct) {
        this.question = question;
        this.answer = answer;
        this.correct = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Boolean isCorrect() {
        return correct;
    }
}
