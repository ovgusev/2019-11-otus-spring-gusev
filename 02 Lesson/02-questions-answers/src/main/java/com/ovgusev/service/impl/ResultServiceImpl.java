package com.ovgusev.service.impl;

import com.ovgusev.constants.MessagesConsts;
import com.ovgusev.domain.Answer;
import com.ovgusev.service.CommandLineIOService;
import com.ovgusev.service.I18nMessageService;
import com.ovgusev.service.ResultService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {
    private final I18nMessageService messageService;
    private final CommandLineIOService commandLineIOService;

    public ResultServiceImpl(I18nMessageService messageService, CommandLineIOService commandLineIOService) {
        this.messageService = messageService;
        this.commandLineIOService = commandLineIOService;
    }

    @Override
    public void printResults(String userName, List<Answer> answers) {
        commandLineIOService.printLine(messageService.getMessage(MessagesConsts.RESULT_START_PROPERTY, userName));

        answers.forEach(answer -> commandLineIOService.printLine(
                messageService.getMessage(MessagesConsts.RESULT_LINE_PROPERTY, answer.getQuestion().getQuestion(), answer.getAnswer(), getBooleanLocalizedString(answer.isCorrect()))
        ));

        commandLineIOService.printLine(
                messageService.getMessage(MessagesConsts.RESULT_SUMMARY_PROPERTY, getCorrectAnswersCount(answers), getWrongAnswersCount(answers))
        );
    }

    private long getCorrectAnswersCount(List<Answer> answers) {
        return answers.stream().filter(Answer::isCorrect).count();
    }

    private long getWrongAnswersCount(List<Answer> answers) {
        return answers.stream().filter(answer -> !answer.isCorrect()).count();
    }

    private String getBooleanLocalizedString(boolean aBoolean) {
        return aBoolean ? messageService.getMessage(MessagesConsts.BOOLEAN_TRUE_PROPERTY) : messageService.getMessage(MessagesConsts.BOOLEAN_FALSE_PROPERTY);
    }
}
