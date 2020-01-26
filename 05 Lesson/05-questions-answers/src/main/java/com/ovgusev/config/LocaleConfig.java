package com.ovgusev.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Optional;

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
        Locale locale = new Locale(
                Optional.ofNullable(language).orElseGet(() -> Locale.getDefault().getLanguage()),
                Optional.ofNullable(country).orElseGet(() -> Locale.getDefault().getCountry()));

        LocaleContextHolder.setLocale(locale);
    }
}
