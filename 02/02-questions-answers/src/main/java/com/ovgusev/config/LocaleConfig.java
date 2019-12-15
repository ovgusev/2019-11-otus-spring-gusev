package com.ovgusev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Optional;

@Configuration
public class LocaleConfig {
    @Value("${user.language}")
    private String defaultLanguage;

    @Value("${user.country}")
    private String defaultCountry;

    @Value("${language:#{null}}")
    private Optional<String> language;

    @Value("${country:#{null}}")
    private Optional<String> country;

    @Value("${file.basename}")
    private String fileBasename;

    public LocaleConfig() {
    }

    public LocaleConfig(String defaultLanguage, String defaultCountry, Optional<String> language, Optional<String> country, String fileBasename) {
        this.defaultLanguage = defaultLanguage;
        this.defaultCountry = defaultCountry;
        this.language = language;
        this.country = country;
        this.fileBasename = fileBasename;
    }

    public Locale getLocale() {
        return new Locale(language.orElse(defaultLanguage), country.orElse(defaultCountry));
    }

    public String getCSVFileName() {
        return fileBasename + "_" + getLocale().getLanguage() + ".csv";
    }
}
