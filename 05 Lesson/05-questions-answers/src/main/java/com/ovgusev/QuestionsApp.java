package com.ovgusev;

import com.ovgusev.config.LocaleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LocaleConfig.class)
public class QuestionsApp {
    public static void main(String[] args) {
        SpringApplication.run(QuestionsApp.class);
    }
}
