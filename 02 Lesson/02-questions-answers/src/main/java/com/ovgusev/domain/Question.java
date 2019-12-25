package com.ovgusev.domain;

import java.util.List;
import java.util.Objects;

public class Question {
    private String question;
    private List<String> answers;

    public Question(String question, List<String> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question q = (Question) o;
        return Objects.equals(getQuestion(), q.getQuestion()) && Objects.equals(getAnswers(), q.getAnswers());
    }
}
