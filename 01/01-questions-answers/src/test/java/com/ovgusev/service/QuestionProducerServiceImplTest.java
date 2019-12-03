package com.ovgusev.service;

import com.ovgusev.domain.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing QuestionProducerServiceImpl class")
class QuestionProducerServiceImplTest {

    @Test
    @DisplayName("Should read existing csv file correctly")
    void shouldParseCorrect() {
        QuestionProducerService questionProducerService = new QuestionProducerServiceImpl("test-questions-answers.csv");
        assertEquals(
                List.of(new Question("Q1", List.of("A1")),
                        new Question("Q2", List.of("A2", "AA2.1"))),
                questionProducerService.getQuestions());
    }
}