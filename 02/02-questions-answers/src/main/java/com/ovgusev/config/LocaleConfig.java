package com.ovgusev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
public class LocaleConfig {
    private final String defaultLanguage;

    private final String defaultCountry;

    private final String language;

    private final String country;

    private final String fileBasename;

    private final Locale locale;

    private final String CSVFileName;

    public LocaleConfig(@Value("${user.language}") String defaultLanguage,
                        @Value("${user.country}") String defaultCountry,
                        @Value("${language:#{null}}") String language,
                        @Value("${country:#{null}}") String country,
                        @Value("${file.basename}") String fileBasename) {
        this.defaultLanguage = defaultLanguage;
        this.defaultCountry = defaultCountry;
        this.language = language;
        this.country = country;
        this.fileBasename = fileBasename;
        this.locale = calcLocale();
        this.CSVFileName = calcCSVFileName();
    }

    public Locale getLocale() {
        return locale;
    }

    public String getCSVFileName() {
        return CSVFileName;
    }

    private Locale calcLocale() {
        return new Locale(Optional.ofNullable(language).orElse(defaultLanguage), Optional.ofNullable(country).orElse(defaultCountry));
    }

    private String calcCSVFileName() {
        return fileBasename + "_" + getLocale().getLanguage() + ".csv";
    }
}
