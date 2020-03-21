package com.ovgusev.shell;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import static com.ovgusev.config.JobConfig.MIGRATION_JOB_NAME;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {
    private final JobOperator jobOperator;
    private final JobExplorer jobExplorer;

    @SneakyThrows
    @ShellMethod(value = "startMigrationJob", key = "sm")
    public void startMigrationJob() {
        Long executionId = jobOperator.startNextInstance(MIGRATION_JOB_NAME);
        System.out.println(jobOperator.getSummary(executionId));
    }

    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(MIGRATION_JOB_NAME));
    }
}
