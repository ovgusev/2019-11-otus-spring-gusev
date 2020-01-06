package com.ovgusev.domain;

public class Answer {
    private Question question;
    private String answer;
    private boolean correct;

    public Answer(Question question, String answer, boolean correct) {
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

    public boolean isCorrect() {
        return correct;
    }
}
