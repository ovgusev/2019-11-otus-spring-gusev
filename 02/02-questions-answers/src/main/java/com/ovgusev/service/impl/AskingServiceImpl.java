package com.ovgusev.service.impl;

import com.ovgusev.domain.Answer;
import com.ovgusev.domain.Question;
import com.ovgusev.service.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AskingServiceImpl implements AskingService {
    private final StudentIOService studentIOService;
    private final QuestionProducerService questionProducerService;
    private final ResultService resultService;
    private final ResourceService resourceService;

    public AskingServiceImpl(StudentIOService studentIOService, QuestionProducerService questionProducerService, ResultService resultService, ResourceService resourceService) {
        this.studentIOService = studentIOService;
        this.questionProducerService = questionProducerService;
        this.resultService = resultService;
        this.resourceService = resourceService;
    }

    @Override
    public void askQuestions() {
        List<Answer> answers = new ArrayList<>();
        String userName = getUserName();

        questionProducerService.getQuestions().forEach(question -> {
            studentIOService.printLine(question.getQuestion());
            String answer = studentIOService.readLine();
            answers.add(new Answer(question, answer, isAnswerCorrect(answer, question)));
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

    private String getUserName() {
        studentIOService.printLine(resourceService.getAskName());
        return studentIOService.readLine();
    }
}
