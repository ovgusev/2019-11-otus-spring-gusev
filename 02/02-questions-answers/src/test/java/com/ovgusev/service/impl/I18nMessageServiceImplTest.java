package com.ovgusev.service.impl;

import com.ovgusev.service.I18nMessageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Testing I18nMessageServiceImpl class")
class I18nMessageServiceImplTest {
    private final static String DEFAULT_LANGUAGE = "DEFAULT_LANGUAGE";
    private final static String DEFAULT_COUNTRY = "DEFAULT_COUNTRY";
    private final static String LANGUAGE = "LANGUAGE";
    private final static String COUNTRY = "COUNTRY";

    private static final MessageSource messageSource = mock(MessageSource.class);

    @BeforeAll
    public static void init() {
        when(messageSource.getMessage(any(), any(), any())).thenAnswer(invocationOnMock -> {
            Locale locale = invocationOnMock.getArgument(2);
            return locale.toString();
        });
    }

    @Test
    @DisplayName("Should use default Locale correct")
    void shouldUseDefaultLocaleCorrect() {
        I18nMessageService messageService = new I18nMessageServiceImpl(messageSource, DEFAULT_LANGUAGE, DEFAULT_COUNTRY, Optional.ofNullable(null), Optional.ofNullable(null));
        assertEquals(new Locale(DEFAULT_LANGUAGE, DEFAULT_COUNTRY).toString(),
                messageService.getMessage("any"));
    }

    @Test
    @DisplayName("Should use Locale correct")
    void shouldUseLocaleCorrect() {
        I18nMessageService messageService = new I18nMessageServiceImpl(messageSource, DEFAULT_LANGUAGE, DEFAULT_COUNTRY, Optional.of(LANGUAGE), Optional.of(COUNTRY));
        assertEquals(new Locale(LANGUAGE, COUNTRY).toString(),
                messageService.getMessage("any"));
    }
}