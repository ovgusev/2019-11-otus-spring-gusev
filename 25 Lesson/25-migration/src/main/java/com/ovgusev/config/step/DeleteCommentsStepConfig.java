package com.ovgusev.config.step;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DeleteCommentsStepConfig {
    private final StepBuilderFactory stepBuilderFactory;
    private final JdbcOperations jdbcOperations;

    public static final String DELETE_COMMENTS_STEP = "DELETE_COMMENTS_STEP";

    @StepScope
    @Bean
    public Tasklet deleteCommentsTasklet() {
        return (contribution, chunkContext) -> {
            jdbcOperations.update("delete from comments");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean(DELETE_COMMENTS_STEP)
    public Step deleteCommentsStep(Tasklet tasklet) {
        return stepBuilderFactory.get(DELETE_COMMENTS_STEP)
                .tasklet(tasklet)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        log.info("Начало удаления комментариев");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        log.info("Конец удаления комментариев");
                        return ExitStatus.COMPLETED;
                    }
                })
                .build();
    }
}
