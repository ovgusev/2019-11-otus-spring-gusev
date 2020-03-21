package com.ovgusev.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import com.ovgusev.changelogs.InitMongoDBDataChangeLog;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MongoConfig {
    private final MongoProperties mongoProperties;

    @Bean
    public Mongock mongock(MongoClient mongoClient) {
        return new SpringMongockBuilder(mongoClient, mongoProperties.getDatabase(), InitMongoDBDataChangeLog.class.getPackageName())
                .build();
    }
}
