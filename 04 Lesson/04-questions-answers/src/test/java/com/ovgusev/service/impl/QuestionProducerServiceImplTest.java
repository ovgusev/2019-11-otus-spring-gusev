package com.ovgusev.service.impl;

import com.ovgusev.domain.Question;
import com.ovgusev.service.QuestionProducerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing QuestionProducerServiceImpl class")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class QuestionProducerServiceImplTest {
    @Autowired
    private QuestionProducerService questionProducerService;

    @Test
    @DisplayName("Should read existing test file correct")
    void shouldParseCorrect() {
        assertEquals(
                List.of(new Question("Q1", List.of("A1")),
                        new Question("Q2", List.of("A2", "AA2.1"))),
                questionProducerService.getQuestions());
    }
}