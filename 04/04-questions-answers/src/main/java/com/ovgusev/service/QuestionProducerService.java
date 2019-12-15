package com.ovgusev.service;

import com.ovgusev.domain.Question;

import java.util.List;

public interface QuestionProducerService {
    List<Question> getQuestions();
}
