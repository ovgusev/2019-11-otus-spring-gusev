package com.ovgusev.config.step;

import com.ovgusev.domain.nosql.NoSqlBook;
import com.ovgusev.domain.sql.Author;
import com.ovgusev.domain.sql.Book;
import com.ovgusev.domain.sql.Genre;
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
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MigrateBooksStepConfig {
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final MongoOperations mongoOperations;

    public static final String MIGRATE_BOOKS_STEP = "MIGRATE_BOOKS_STEP";

    @StepScope
    @Bean
    public MongoItemReader<NoSqlBook> bookMigrationMongoItemReader() {
        return new MongoItemReaderBuilder<NoSqlBook>().name("bookMigrationMongoItemReader")
                .template(mongoOperations)
                .targetType(NoSqlBook.class)
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<NoSqlBook, Book> bookMigrationProcessor() {
        return book -> Book.of(book.getName(), Author.of(book.getAuthor()), Genre.of(book.getGenre()));
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter<Book> bookMigrationItemWriter() {
        return new JdbcBatchItemWriterBuilder<Book>()
                .dataSource(dataSource)
                .sql("merge into books(name, author_id, genre_id) key(name) select ?, (select id from authors where name = ?), (select id from genres where name = ?)") // h2 merge syntax
                .itemPreparedStatementSetter((book, preparedStatement) -> {
                    preparedStatement.setString(1, book.getName());
                    preparedStatement.setString(2, book.getAuthor().getName());
                    preparedStatement.setString(3, book.getGenre().getName());
                })
                .build();
    }

    @Bean(MIGRATE_BOOKS_STEP)
    public Step bookMigrationStep(ItemReader<NoSqlBook> reader, ItemProcessor<NoSqlBook, Book> processor, ItemWriter<Book> writer) {
        return stepBuilderFactory.get(MIGRATE_BOOKS_STEP)
                .allowStartIfComplete(true)
                .<NoSqlBook, Book>chunk(10)
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
                .listener(new ItemProcessListener<NoSqlBook, Book>() {
                    @Override
                    public void beforeProcess(NoSqlBook book) {
                        log.info("Начало обработки {}", book);
                    }

                    @Override
                    public void afterProcess(NoSqlBook book, Book book2) {
                        log.info("Конец обработки {}, {}", book, book2);
                    }

                    @Override
                    public void onProcessError(NoSqlBook book, Exception e) {
                        log.error("Ошибка обработки {}", book);
                    }
                })
                .listener(new ItemWriteListener<Book>() {
                    @Override
                    public void beforeWrite(List<? extends Book> list) {
                        log.info("Начало записи {}", list);
                    }

                    @Override
                    public void afterWrite(List<? extends Book> list) {
                        log.info("Конец записи {}", list);
                    }

                    @Override
                    public void onWriteError(Exception e, List<? extends Book> list) {
                        log.error("Ошибка записи {}", list);
                    }
                })
                .build();
    }
}
