package com.ovgusev.config.step;

import com.ovgusev.domain.nosql.NoSqlBook;
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
public class MigrateGenresStepConfig {
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    public static final String MIGRATE_GENRES_STEP = "MIGRATE_GENRES_STEP";

    @StepScope
    @Bean
    public ItemProcessor<NoSqlBook, Genre> genreMigrationProcessor() {
        Set<Genre> genres = new HashSet<>();

        return book -> {
            Genre genre = Genre.of(book.getGenre());

            if (genres.add(genre)) {
                return genre;
            } else {
                return null;
            }
        };
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter<Genre> genreMigrationItemWriter() {
        return new JdbcBatchItemWriterBuilder<Genre>()
                .dataSource(dataSource)
                .sql("merge into genres(name) key(name) values (?)") // h2 merge syntax
                .itemPreparedStatementSetter((genre, preparedStatement) -> {
                    preparedStatement.setString(1, genre.getName());
                })
                .build();
    }

    @Bean(MIGRATE_GENRES_STEP)
    public Step bookMigrationStep(ItemReader<NoSqlBook> reader, ItemProcessor<NoSqlBook, Genre> processor, ItemWriter<Genre> writer) {
        return stepBuilderFactory.get(MIGRATE_GENRES_STEP)
                .allowStartIfComplete(true)
                .<NoSqlBook, Genre>chunk(10)
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
                .listener(new ItemProcessListener<NoSqlBook, Genre>() {
                    @Override
                    public void beforeProcess(NoSqlBook book) {
                        log.info("Начало обработки {}", book);
                    }

                    @Override
                    public void afterProcess(NoSqlBook book, Genre genre) {
                        log.info("Конец обработки {}, {}", book, genre);
                    }

                    @Override
                    public void onProcessError(NoSqlBook book, Exception e) {
                        log.error("Ошибка обработки {}", book);
                    }
                })
                .listener(new ItemWriteListener<Genre>() {
                    @Override
                    public void beforeWrite(List<? extends Genre> list) {
                        log.info("Начало записи {}", list);
                    }

                    @Override
                    public void afterWrite(List<? extends Genre> list) {
                        log.info("Конец записи {}", list);
                    }

                    @Override
                    public void onWriteError(Exception e, List<? extends Genre> list) {
                        log.error("Ошибка записи {}", list);
                    }
                })
                .build();
    }
}
