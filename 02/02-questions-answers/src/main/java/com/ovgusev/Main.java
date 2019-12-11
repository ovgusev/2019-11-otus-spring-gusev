package com.ovgusev;

import com.ovgusev.service.AskingService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractApplicationContext;

@ComponentScan
@PropertySource("classpath:application.properties")
public class Main {
    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        AskingService askingService = context.getBean(AskingService.class);
        askingService.askQuestions();
    }
}
