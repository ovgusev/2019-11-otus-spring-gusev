package com.ovgusev.service.impl;

import com.ovgusev.domain.Question;
import com.ovgusev.service.QuestionProducerService;
import com.ovgusev.service.ResourceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Testing QuestionProducerServiceImpl class")
class QuestionProducerServiceImplTest {
    @Test
    @DisplayName("Should read existing InputStream correct")
    void shouldParseCorrect() throws IOException {
        StringBuilder stringBuilder = new StringBuilder()
                .append("Q1,A1")
                .append("\n")
                .append("Q2,A2,AA2.1");
        InputStream inputStream = new ByteArrayInputStream(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));

        ResourceService resourceServiceMock = mock(ResourceService.class);
        when(resourceServiceMock.getQuestionsAndAnswers()).thenReturn(inputStream);
        QuestionProducerService questionProducerService = new QuestionProducerServiceImpl(resourceServiceMock);
        assertEquals(
                List.of(new Question("Q1", List.of("A1")),
                        new Question("Q2", List.of("A2", "AA2.1"))),
                questionProducerService.getQuestions());
    }
}