package com.ovgusev.service;

import org.springframework.lang.Nullable;

public interface I18nMessageService {
    String getMessage(String code);

    String getMessage(String code, @Nullable Object[] args);
}
