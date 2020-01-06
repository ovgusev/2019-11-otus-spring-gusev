package com.ovgusev.service;

import com.ovgusev.domain.Answer;

import java.util.List;

public interface ResultService {
    String printResults(String userName, List<Answer> answers);
}
