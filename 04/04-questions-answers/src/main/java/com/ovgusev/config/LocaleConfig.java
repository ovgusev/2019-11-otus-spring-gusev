package com.ovgusev.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@ConfigurationProperties
public class LocaleConfig {
    private String language;

    private String country;

    private String fileBasename;

    public String getFileBasename() {
        return fileBasename;
    }

    @ConstructorBinding
    public LocaleConfig(String language, String country, String fileBasename) {
        this.language = language;
        this.country = country;
        this.fileBasename = fileBasename;
        calcLocale();
    }

    private void calcLocale() {
        if (language != null && country != null) {
            Locale locale = new Locale(language, country);
            LocaleContextHolder.setDefaultLocale(locale);
            LocaleContextHolder.setLocale(locale);
        }
    }
}
