package com.ovgusev.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Component
@ConfigurationProperties
public class LocaleConfig {
    private String language;

    private String country;

    private String fileBasename;

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setFileBasename(String fileBasename) {
        this.fileBasename = fileBasename;
    }

    private String CSVFileName;

    @PostConstruct
    public void init() {
        calcLocale();
        this.CSVFileName = calcCSVFileName();
    }

    public String getCSVFileName() {
        return CSVFileName;
    }

    private void calcLocale() {
        if (language != null && country != null) {
            Locale locale = new Locale(language, country);
            LocaleContextHolder.setDefaultLocale(locale);
            LocaleContextHolder.setLocale(locale);
        }
    }

    private String calcCSVFileName() {
        return fileBasename + "_" + LocaleContextHolder.getLocale().getLanguage() + ".csv";
    }
}
