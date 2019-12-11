package com.ovgusev.service;

import com.ovgusev.domain.Answer;
import com.ovgusev.domain.Question;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AskingServiceImpl implements AskingService {
    private final PrintStream out = System.out;
    private final Scanner scanner = new Scanner(System.in);
    private final QuestionProducerService questionProducerService;
    private final ResultService resultService;

    public AskingServiceImpl(QuestionProducerService questionProducerService,
                             ResultService resultService) {
        this.questionProducerService = questionProducerService;
        this.resultService = resultService;
    }

    @Override
    public void askQuestions() {
        List<Answer> answers = new ArrayList<>();

        out.println("Enter your name:");
        String userName = scanner.nextLine();

        questionProducerService.getQuestions().forEach(question -> {
            out.println(question.getQuestion());
            String answer = scanner.nextLine();
            answers.add(new Answer(question.getQuestion(), answer, isAnswerCorrect(answer, question)));
        });

        resultService.printResults(userName, answers);
    }

    private Boolean isAnswerCorrect(String answer, Question question) {
        for (String s : question.getAnswers()) {
            if (s.trim().toLowerCase().equals(answer.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
