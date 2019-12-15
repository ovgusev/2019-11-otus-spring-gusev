package com.ovgusev;

import com.ovgusev.service.ExamService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class QuestionsApp {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(QuestionsApp.class);
        ExamService examService = context.getBean(ExamService.class);
        examService.run();
    }
}
