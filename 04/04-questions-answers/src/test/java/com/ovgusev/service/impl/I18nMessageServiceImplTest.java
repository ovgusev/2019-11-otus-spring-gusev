package com.ovgusev.service.impl;

import com.ovgusev.service.I18nMessageService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("Testing I18nMessageServiceImpl class")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class I18nMessageServiceImplTest {
    private static Locale defaultLocale;
    private final static Locale LOCALE = new Locale("LANGUAGE", "COUNTRY");

    @Autowired
    private I18nMessageService messageService;

    @MockBean
    private MessageSource messageSourceMock;

    @BeforeAll
    public static void init() {
        defaultLocale = LocaleContextHolder.getLocale();
    }

    @BeforeEach
    public void initEach() {
        when(messageSourceMock.getMessage(any(), any(), any())).thenAnswer(invocationOnMock -> {
            Locale locale = invocationOnMock.getArgument(2);
            return locale.toString();
        });
    }

    @Test
    @DisplayName("Should use Locale correct from context")
    void shouldUseLocaleCorrect() {
        LocaleContextHolder.setLocale(LOCALE);
        assertEquals(LOCALE.toString(), messageService.getMessage("any"));
    }

    @AfterAll
    public static void destroy() {
        LocaleContextHolder.setDefaultLocale(defaultLocale);
        LocaleContextHolder.setLocale(defaultLocale);
    }
}