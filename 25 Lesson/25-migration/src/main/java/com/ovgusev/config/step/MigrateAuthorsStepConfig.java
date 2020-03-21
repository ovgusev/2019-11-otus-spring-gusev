package com.ovgusev.config.step;

import com.ovgusev.domain.nosql.NoSqlBook;
import com.ovgusev.domain.sql.Author;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MigrateAuthorsStepConfig {
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    public static final String MIGRATE_AUTHORS_STEP = "MIGRATE_AUTHORS_STEP";

    @StepScope
    @Bean
    public ItemProcessor<NoSqlBook, Author> authorMigrationProcessor() {
        Set<Author> authors = new HashSet<>();

        return book -> {
            Author author = Author.of(book.getAuthor());

            if (authors.add(author)) {
                return author;
            } else {
                return null;
            }
        };
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter<Author> authorMigrationItemWriter() {
        return new JdbcBatchItemWriterBuilder<Author>()
                .dataSource(dataSource)
                .sql("merge into authors(name) key(name) values (?)") // h2 merge syntax
                .itemPreparedStatementSetter((author, preparedStatement) -> {
                    preparedStatement.setString(1, author.getName());
                })
                .build();
    }

    @Bean(MIGRATE_AUTHORS_STEP)
    public Step bookMigrationStep(ItemReader<NoSqlBook> reader, ItemProcessor<NoSqlBook, Author> processor, ItemWriter<Author> writer) {
        return stepBuilderFactory.get(MIGRATE_AUTHORS_STEP)
                .allowStartIfComplete(true)
                .<NoSqlBook, Author>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new ItemReadListener<NoSqlBook>() {
                    @Override
                    public void beforeRead() {
                        log.info("Начало чтения");
                    }

                    @Override
                    public void afterRead(NoSqlBook book) {
                        log.info("Прочли {}", book);
                    }

                    @Override
                    public void onReadError(Exception e) {
                        log.error("Ошибка чтения");
                    }
                })
                .listener(new ItemProcessListener<NoSqlBook, Author>() {
                    @Override
                    public void beforeProcess(NoSqlBook book) {
                        log.info("Начало обработки {}", book);
                    }

                    @Override
                    public void afterProcess(NoSqlBook book, Author author) {
                        log.info("Конец обработки {}, {}", book, author);
                    }

                    @Override
                    public void onProcessError(NoSqlBook book, Exception e) {
                        log.error("Ошибка обработки {}", book);
                    }
                })
                .listener(new ItemWriteListener<Author>() {
                    @Override
                    public void beforeWrite(List<? extends Author> list) {
                        log.info("Начало записи {}", list);
                    }

                    @Override
                    public void afterWrite(List<? extends Author> list) {
                        log.info("Конец записи {}", list);
                    }

                    @Override
                    public void onWriteError(Exception e, List<? extends Author> list) {
                        log.error("Ошибка записи {}", list);
                    }
                })
                .build();
    }
}
