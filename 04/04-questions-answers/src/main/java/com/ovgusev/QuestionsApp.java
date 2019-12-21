package com.ovgusev;

import com.ovgusev.config.LocaleConfig;
import com.ovgusev.service.ExamService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties(LocaleConfig.class)
public class QuestionsApp {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(QuestionsApp.class);
        ExamService examService = context.getBean(ExamService.class);
        examService.run();
    }
}
