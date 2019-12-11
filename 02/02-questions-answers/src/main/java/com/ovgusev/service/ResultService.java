package com.ovgusev.service;

import com.ovgusev.domain.Answer;

import java.util.List;

public interface ResultService {
    void printResults(String userName, List<Answer> answers);
}
