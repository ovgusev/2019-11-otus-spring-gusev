package com.ovgusev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class BooksApp {
    public static void main(String[] args) {
        SpringApplication.run(BooksApp.class);
    }
}
