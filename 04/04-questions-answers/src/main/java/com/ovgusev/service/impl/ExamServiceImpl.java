package com.ovgusev.service.impl;

import com.ovgusev.domain.Answer;
import com.ovgusev.service.AskingService;
import com.ovgusev.service.ExamService;
import com.ovgusev.service.ResultService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl implements ExamService {
    private final AskingService askingService;
    private final ResultService resultService;

    public ExamServiceImpl(AskingService askingService, ResultService resultService) {
        this.askingService = askingService;
        this.resultService = resultService;
    }

    @Override
    public void run() {
        String name = askingService.askName();
        List<Answer> answers = askingService.askQuestions();
        resultService.printResults(name, answers);
    }
}
