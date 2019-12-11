package com.ovgusev.service.impl;

import com.ovgusev.domain.Answer;
import com.ovgusev.service.ResourceService;
import com.ovgusev.service.ResultService;
import com.ovgusev.service.StudentIOService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {
    private final ResourceService resourceService;
    private final StudentIOService studentIOService;

    public ResultServiceImpl(ResourceService resourceService, StudentIOService studentIOService) {
        this.resourceService = resourceService;
        this.studentIOService = studentIOService;
    }

    @Override
    public void printResults(String userName, List<Answer> answers) {
        studentIOService.printLine(resourceService.getResultStart(userName));
        answers.forEach(answer -> studentIOService.printLine(resourceService.getResultLine(answer)));
        studentIOService.printLine(resourceService.getResultSummary(
                answers.stream().filter(Answer::isCorrect).count(),
                answers.stream().filter(answer -> !answer.isCorrect()).count()
        ));
    }
}
