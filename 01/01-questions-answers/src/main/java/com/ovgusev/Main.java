package com.ovgusev;

import com.ovgusev.service.AskingService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        AskingService askingService = context.getBean(AskingService.class);
        askingService.askQuestions();
    }
}
