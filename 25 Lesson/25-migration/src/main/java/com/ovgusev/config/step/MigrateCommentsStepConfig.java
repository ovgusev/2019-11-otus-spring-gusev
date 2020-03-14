package com.ovgusev.config.step;

import com.ovgusev.domain.nosql.NoSqlComment;
import com.ovgusev.domain.sql.Book;
import com.ovgusev.domain.sql.Comment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MigrateCommentsStepConfig {
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final MongoOperations mongoOperations;

    public static final String MIGRATE_COMMENTS_STEP = "MIGRATE_COMMENTS_STEP";

    @StepScope
    @Bean
    public MongoItemReader<NoSqlComment> commentMigrationMongoItemReader() {
        return new MongoItemReaderBuilder<NoSqlComment>().name("commentMigrationMongoItemReader")
                .template(mongoOperations)
                .targetType(NoSqlComment.class)
                .jsonQuery("{}")
                .sorts(new HashMap<>())
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<NoSqlComment, Comment> commentMigrationProcessor() {
        return comment -> Comment.of(Book.of(comment.getBook().getName(), null, null), comment.getText());
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter<Comment> commentMigrationItemWriter() {
        return new JdbcBatchItemWriterBuilder<Comment>()
                .dataSource(dataSource)
                .sql("insert into comments(book_id, text, insert_date)" +
                        " select id, ?, CURRENT_DATE()" +
                        " from books" +
                        " where name = ?")
                .itemPreparedStatementSetter(new ItemPreparedStatementSetter<Comment>() {
                    @Override
                    public void setValues(Comment comment, PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setString(1, comment.getText());
                        preparedStatement.setString(2, comment.getBook().getName());
                    }
                })
                .build();
    }

    @Bean(MIGRATE_COMMENTS_STEP)
    public Step commentMigrationStep(MongoItemReader<NoSqlComment> reader, ItemProcessor<NoSqlComment, Comment> processor, JdbcBatchItemWriter<Comment> writer) {
        return stepBuilderFactory.get(MIGRATE_COMMENTS_STEP)
                .allowStartIfComplete(true)
                .<NoSqlComment, Comment>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new ItemReadListener<NoSqlComment>() {
                    @Override
                    public void beforeRead() {
                        log.info("Начало чтения");
                    }

                    @Override
                    public void afterRead(NoSqlComment comment) {
                        log.info("Прочли {}", comment);
                    }

                    @Override
                    public void onReadError(Exception e) {
                        log.error("Ошибка чтения");
                    }
                })
                .listener(new ItemProcessListener<NoSqlComment, Comment>() {
                    @Override
                    public void beforeProcess(NoSqlComment comment) {
                        log.info("Начало обработки {}", comment);
                    }

                    @Override
                    public void afterProcess(NoSqlComment comment, Comment comment2) {
                        log.info("Конец обработки {}, {}", comment, comment2);
                    }

                    @Override
                    public void onProcessError(NoSqlComment comment, Exception e) {
                        log.error("Ошибка обработки {}", comment);
                    }
                })
                .listener(new ItemWriteListener<Comment>() {
                    @Override
                    public void beforeWrite(List<? extends Comment> list) {
                        log.info("Начало записи {}", list);
                    }

                    @Override
                    public void afterWrite(List<? extends Comment> list) {
                        log.info("Конец записи {}", list);
                    }

                    @Override
                    public void onWriteError(Exception e, List<? extends Comment> list) {
                        log.error("Ошибка записи {}", list);
                    }
                })
                .build();
    }
}
