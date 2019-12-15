package com.ovgusev.service.impl;

import com.ovgusev.config.LocaleConfig;
import com.ovgusev.service.I18nMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Testing I18nMessageServiceImpl class")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class I18nMessageServiceImplTest {
    private final static String DEFAULT_LANGUAGE = "DEFAULT_LANGUAGE";
    private final static String DEFAULT_COUNTRY = "DEFAULT_COUNTRY";
    private final static String LANGUAGE = "LANGUAGE";
    private final static String COUNTRY = "COUNTRY";

    @MockBean
    private MessageSource messageSourceMock;

    @BeforeEach
    public void init() {
        when(messageSourceMock.getMessage(any(), any(), any())).thenAnswer(invocationOnMock -> {
            Locale locale = invocationOnMock.getArgument(2);
            return locale.toString();
        });
    }

    @Test
    @DisplayName("Should use default Locale correct")
    void shouldUseDefaultLocaleCorrect() {
        I18nMessageService messageService = new I18nMessageServiceImpl(messageSourceMock, new LocaleConfig(DEFAULT_LANGUAGE, DEFAULT_COUNTRY, Optional.ofNullable(null), Optional.ofNullable(null), null));
        assertEquals(new Locale(DEFAULT_LANGUAGE, DEFAULT_COUNTRY).toString(),
                messageService.getMessage("any"));
    }

    @Test
    @DisplayName("Should use Locale correct")
    void shouldUseLocaleCorrect() {
        I18nMessageService messageService = new I18nMessageServiceImpl(messageSourceMock, new LocaleConfig(DEFAULT_LANGUAGE, DEFAULT_COUNTRY, Optional.of(LANGUAGE), Optional.of(COUNTRY), null));
        assertEquals(new Locale(LANGUAGE, COUNTRY).toString(),
                messageService.getMessage("any"));
    }
}