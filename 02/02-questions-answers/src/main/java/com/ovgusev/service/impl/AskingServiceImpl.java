package com.ovgusev.service.impl;

import com.ovgusev.constants.MessagesConsts;
import com.ovgusev.domain.Answer;
import com.ovgusev.domain.Question;
import com.ovgusev.service.AskingService;
import com.ovgusev.service.CommandLineIOService;
import com.ovgusev.service.I18nMessageService;
import com.ovgusev.service.QuestionProducerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AskingServiceImpl implements AskingService {
    private final CommandLineIOService commandLineIOService;
    private final QuestionProducerService questionProducerService;
    private final I18nMessageService messageService;

    public AskingServiceImpl(CommandLineIOService commandLineIOService, QuestionProducerService questionProducerService, I18nMessageService messageService) {
        this.commandLineIOService = commandLineIOService;
        this.questionProducerService = questionProducerService;
        this.messageService = messageService;
    }

    @Override
    public List<Answer> askQuestions() {
        List<Answer> answers = new ArrayList<>();

        questionProducerService.getQuestions().forEach(question -> {
            commandLineIOService.printLine(question.getQuestion());
            String answer = commandLineIOService.readLine();
            answers.add(new Answer(question, answer, isAnswerCorrect(answer, question)));
        });

        return answers;
    }

    private boolean isAnswerCorrect(String answer, Question question) {
        for (String s : question.getAnswers()) {
            if (s.trim().toLowerCase().equals(answer.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String askName() {
        commandLineIOService.printLine(messageService.getMessage(MessagesConsts.ASK_NAME_PROPERTY));
        return commandLineIOService.readLine();
    }
}
