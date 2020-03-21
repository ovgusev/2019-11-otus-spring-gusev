package com.ovgusev.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import com.ovgusev.changelogs.InitMongoDBDataChangeLog;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@RequiredArgsConstructor
public class MongoConfig {
    private final MongoTemplate mongoTemplate;

    @Bean
    public Mongock mongock(MongoClient mongoClient) {
        return new SpringMongockBuilder(mongoTemplate, InitMongoDBDataChangeLog.class.getPackageName())
                .build();
    }
}
