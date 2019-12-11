package com.ovgusev.service.impl;

import com.ovgusev.domain.Answer;
import com.ovgusev.service.I18nMessageService;
import com.ovgusev.service.ResourceService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ResourceServiceImpl implements ResourceService {
    private final ResourceLoader resourceLoader;
    private final I18nMessageService messageService;

    public ResourceServiceImpl(ResourceLoader resourceLoader, I18nMessageService messageService) {
        this.resourceLoader = resourceLoader;
        this.messageService = messageService;
    }

    @Override
    public String getAskName() {
        return messageService.getMessage("ask.name");
    }

    @Override
    public String getResultStart(String userName) {
        return messageService.getMessage("result.start", new Object[]{userName}) + ":";
    }

    @Override
    public String getResultLine(Answer answer) {
        return String.format(messageService.getMessage("result.line"), answer.getQuestion().getQuestion(), answer.getAnswer(), getBooleanLocalizedString(answer.isCorrect()));
    }

    @Override
    public String getResultSummary(Long correctAnswers, Long wrongAnswers) {
        return String.format(messageService.getMessage("result.summary"), correctAnswers, wrongAnswers);
    }

    @Override
    public InputStream getQuestionsAndAnswers() throws IOException {
        String filePath = messageService.getMessage("file.path");
        return resourceLoader.getResource(filePath).getInputStream();
    }

    private String getBooleanLocalizedString(Boolean aBoolean) {
        return aBoolean ? messageService.getMessage("boolean.true") : messageService.getMessage("boolean.false");
    }
}
