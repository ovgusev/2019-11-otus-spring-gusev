package com.ovgusev.repository.impl;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;
import com.ovgusev.repository.BookRepository;
import com.ovgusev.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest(includeFilters = @ComponentScan.Filter(Component.class))
@DisplayName("Testing methods of class CommentRepository")
class CommentRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    private static final String NOT_EXISTING_ID = "NOT_EXISTING_ID";
    private static final String EXAMPLE_COMMENT = "EXAMPLE_COMMENT";

    private static final Book FIRST_BOOK = Book.of("Test book", "Test author", "Test genre");
    private static final Comment FIRST_COMMENT = Comment.of(FIRST_BOOK, "Test comment");

    @BeforeEach
    void init() {
        // deleteAll не вызывает onBeforeDelete %)
        bookRepository.findAll().forEach(book -> bookRepository.delete(book));

        assertThat(commentRepository.findAll()).isEmpty();
        bookRepository.save(FIRST_BOOK);
        commentRepository.save(FIRST_COMMENT);
    }

    @Test
    @DisplayName("Поиск несуществующей записи по id")
    void shouldfindByIdEmpty() {
        assertEquals(Optional.empty(), commentRepository.findById(NOT_EXISTING_ID));
    }

    @Test
    @DisplayName("Вставка и поиск существующей записи по id")
    void shouldFindByIdCorrect() {
        Comment comment = commentRepository.save(Comment.of(FIRST_BOOK, EXAMPLE_COMMENT));
        assertEquals(comment, commentRepository.findById(comment.getId()).orElse(null));
    }

    @Test
    @DisplayName("Вставка и поиск существующей записи по названию книги")
    void shouldFindByBookName() {
        Comment comment = commentRepository.save(Comment.of(FIRST_BOOK, EXAMPLE_COMMENT));
        List<Comment> commentList = commentRepository.findByBookId(comment.getBook().getId());

        assertEquals(2, commentList.size());
        assertEquals(FIRST_COMMENT, commentList.get(0));
        assertEquals(comment, commentList.get(1));
    }

    @Test
    @DisplayName("Вставка новой записи")
    void shouldInsertCorrect() {
        Comment comment = commentRepository.save(Comment.of(FIRST_BOOK, EXAMPLE_COMMENT));
        assertEquals(comment, commentRepository.findById(comment.getId()).get());
    }

    @Test
    @DisplayName("Удаление записи")
    void shouldDeleteCorrect() {
        assertThat(commentRepository.findById(FIRST_COMMENT.getId())).get().isEqualTo(FIRST_COMMENT);
        commentRepository.delete(FIRST_COMMENT);
        assertThat(commentRepository.findById(FIRST_COMMENT.getId())).isEmpty();
    }
}