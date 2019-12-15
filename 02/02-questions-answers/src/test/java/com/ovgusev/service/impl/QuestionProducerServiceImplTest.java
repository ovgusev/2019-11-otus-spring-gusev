package com.ovgusev.service.impl;

import com.ovgusev.config.LocaleConfig;
import com.ovgusev.domain.Question;
import com.ovgusev.service.QuestionProducerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

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
    private static final String FILE_PATH = "file_path";

    @Test
    @DisplayName("Should read existing InputStream correct")
    void shouldParseCorrect() throws IOException {
        StringBuilder stringBuilder = new StringBuilder()
                .append("Q1,A1")
                .append("\n")
                .append("Q2,A2,AA2.1");
        InputStream inputStream = new ByteArrayInputStream(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));

        LocaleConfig localeConfigMock = mock(LocaleConfig.class);
        ResourceLoader resourceLoaderMock = mock(ResourceLoader.class);
        Resource resourceMock = mock(Resource.class);

        when(localeConfigMock.getCSVFileName()).thenReturn(FILE_PATH);
        when(resourceLoaderMock.getResource(FILE_PATH)).thenReturn(resourceMock);
        when(resourceMock.getInputStream()).thenReturn(inputStream);

        QuestionProducerService questionProducerService = new QuestionProducerServiceImpl(resourceLoaderMock, localeConfigMock);
        assertEquals(
                List.of(new Question("Q1", List.of("A1")),
                        new Question("Q2", List.of("A2", "AA2.1"))),
                questionProducerService.getQuestions());
    }
}