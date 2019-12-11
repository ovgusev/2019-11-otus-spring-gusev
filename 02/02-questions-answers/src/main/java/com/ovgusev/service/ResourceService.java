package com.ovgusev.service;

import com.ovgusev.domain.Answer;

import java.io.IOException;
import java.io.InputStream;

public interface ResourceService {
    String getAskName();

    String getResultStart(String userName);

    String getResultLine(Answer answer);

    String getResultSummary(Long correctAnswers, Long wrongAnswers);

    InputStream getQuestionsAndAnswers() throws IOException;
}
