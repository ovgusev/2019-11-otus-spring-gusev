package com.ovgusev.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ovgusev.config.step.DeleteCommentsStepConfig.DELETE_COMMENTS_STEP;
import static com.ovgusev.config.step.MigrateAuthorsStepConfig.MIGRATE_AUTHORS_STEP;
import static com.ovgusev.config.step.MigrateBooksStepConfig.MIGRATE_BOOKS_STEP;
import static com.ovgusev.config.step.MigrateCommentsStepConfig.MIGRATE_COMMENTS_STEP;
import static com.ovgusev.config.step.MigrateGenresStepConfig.MIGRATE_GENRES_STEP;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobConfig {
    private final JobBuilderFactory jobBuilderFactory;

    public static final String MIGRATION_JOB_NAME = "MIGRATION_JOB_NAME";

    @Bean
    public Job migrationJob(@Qualifier(MIGRATE_GENRES_STEP) Step migrateGenresStep,
                            @Qualifier(MIGRATE_AUTHORS_STEP) Step migrateAuthorsStep,
                            @Qualifier(MIGRATE_BOOKS_STEP) Step migrateBooksStep,
                            @Qualifier(DELETE_COMMENTS_STEP) Step deleteCommentsStep,
                            @Qualifier(MIGRATE_COMMENTS_STEP) Step migrateCommentsStep) {
        return jobBuilderFactory.get(MIGRATION_JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .flow(migrateGenresStep)
                .next(migrateAuthorsStep)
                .next(migrateBooksStep)
                .next(deleteCommentsStep)
                .next(migrateCommentsStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        log.info("Начало выполнения job");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("Конец выполнения job");
                    }
                })
                .build();
    }

}
