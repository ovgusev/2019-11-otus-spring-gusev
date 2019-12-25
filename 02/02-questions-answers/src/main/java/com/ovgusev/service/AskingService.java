package com.ovgusev.service;

import com.ovgusev.domain.Answer;

import java.util.List;

public interface AskingService {
    String askName();

    List<Answer> askQuestions();
}
