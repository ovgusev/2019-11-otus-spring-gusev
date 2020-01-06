package com.ovgusev.service.impl;

import com.ovgusev.config.LocaleConfig;
import com.ovgusev.service.I18nMessageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;

import java.util.Locale;

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

    private static final MessageSource messageSourceMock = mock(MessageSource.class);

    @BeforeAll
    public static void init() {
        when(messageSourceMock.getMessage(any(), any(), any())).thenAnswer(invocationOnMock -> {
            Locale locale = invocationOnMock.getArgument(2);
            return locale.toString();
        });
    }

    @Test
    @DisplayName("Should use default Locale correct")
    void shouldUseDefaultLocaleCorrect() {
        I18nMessageService messageService = new I18nMessageServiceImpl(messageSourceMock, new LocaleConfig(DEFAULT_LANGUAGE, DEFAULT_COUNTRY, null, null, null));
        assertEquals(new Locale(DEFAULT_LANGUAGE, DEFAULT_COUNTRY).toString(),
                messageService.getMessage("any"));
    }

    @Test
    @DisplayName("Should use Locale correct")
    void shouldUseLocaleCorrect() {
        I18nMessageService messageService = new I18nMessageServiceImpl(messageSourceMock, new LocaleConfig(DEFAULT_LANGUAGE, DEFAULT_COUNTRY, LANGUAGE, COUNTRY, null));
        assertEquals(new Locale(LANGUAGE, COUNTRY).toString(),
                messageService.getMessage("any"));
    }
}