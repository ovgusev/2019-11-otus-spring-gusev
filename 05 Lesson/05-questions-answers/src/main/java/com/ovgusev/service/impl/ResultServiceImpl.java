package com.ovgusev.service.impl;

import com.ovgusev.constants.MessagesConsts;
import com.ovgusev.domain.Answer;
import com.ovgusev.service.I18nMessageService;
import com.ovgusev.service.ResultService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ResultServiceImpl implements ResultService {
    private final I18nMessageService messageService;

    public ResultServiceImpl(I18nMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public String printResults(String userName, List<Answer> answers) {
        return Stream.of(
                messageService.getMessage(MessagesConsts.RESULT_START_PROPERTY, userName),
                answers.stream()
                        .map(answer -> messageService.getMessage(MessagesConsts.RESULT_LINE_PROPERTY, answer.getQuestion().getQuestion(), answer.getAnswer(), getBooleanLocalizedString(answer.isCorrect())))
                        .collect(Collectors.joining(System.getProperty("line.separator"))),
                messageService.getMessage(MessagesConsts.RESULT_SUMMARY_PROPERTY, getCorrectAnswersCount(answers), getWrongAnswersCount(answers))
        ).collect(Collectors.joining(System.getProperty("line.separator")));
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
