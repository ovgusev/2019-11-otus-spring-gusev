package com.ovgusev.service.impl;

import com.ovgusev.service.I18nMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class I18nMessageServiceImpl implements I18nMessageService {
    private final MessageSource messageSource;
    private final Locale locale;

    public I18nMessageServiceImpl(MessageSource messageSource,
                                  @Value("${user.language}") String defaultLanguage,
                                  @Value("${user.country}") String defaultCountry,
                                  @Value("${language:#{null}}") Optional<String> language,
                                  @Value("${country:#{null}}") Optional<String> country) {
        this.messageSource = messageSource;
        this.locale = new Locale(language.orElse(defaultLanguage), country.orElse(defaultCountry));
    }

    @Override
    public String getMessage(String code) {
        return getMessage(code, null);
    }

    @Override
    public String getMessage(String code, @Nullable Object[] args) {
        return messageSource.getMessage(code, args, locale);
    }
}
