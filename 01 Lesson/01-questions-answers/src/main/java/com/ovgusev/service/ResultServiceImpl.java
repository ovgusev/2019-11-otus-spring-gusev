package com.ovgusev.service;

import com.ovgusev.domain.Answer;

import java.io.PrintStream;
import java.util.List;

public class ResultServiceImpl implements ResultService {
    private final PrintStream out = System.out;

    @Override
    public void printResults(String userName, List<Answer> answers) {
        out.println("Your testing results:");
        answers.forEach(answer -> out.printf("Question: %s\tAnswer: %s\t - %s\n", answer.getQuestion(), answer.getAnswer(), answer.getCorrect().toString()));
        out.printf("Total results: %d correct, %d wrong",
                answers.stream().filter(Answer::getCorrect).count(),
                answers.stream().filter(answer -> !answer.getCorrect()).count()
        );
    }
}
