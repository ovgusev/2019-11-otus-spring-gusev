package com.ovgusev.service.impl;

import com.ovgusev.config.LocaleConfig;
import com.ovgusev.service.I18nMessageService;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class I18nMessageServiceImpl implements I18nMessageService {
    private final MessageSource messageSource;
    private final LocaleConfig localeConfig;

    public I18nMessageServiceImpl(MessageSource messageSource, LocaleConfig localeConfig) {
        this.messageSource = messageSource;
        this.localeConfig = localeConfig;
    }

    @Override
    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, localeConfig.getLocale());
    }
}
