package com.ovgusev.repository.impl;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;
import com.ovgusev.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DisplayName("Testing methods of class CommentRepositoryJpa")
class CommentRepositoryJpaTest {
    @Autowired
    private TestEntityManager em;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    CommentRepository repository;

    private static final long NOT_EXISTING_ID = -1L;
    private static final String EXAMPLE_COMMENT = "EXAMPLE_COMMENT";

    private Book firstBook;

    @BeforeEach
    void init() {
        firstBook = em.find(Book.class, 1L);
    }

    @Test
    @DisplayName("Поиск несуществующей записи по id")
    void shouldfindByIdEmpty() {
        assertEquals(Optional.empty(), repository.findById(NOT_EXISTING_ID));
    }

    @Test
    @DisplayName("Вставка и поиск существующей записи по id")
    void shouldFindByIdCorrect() {
        Comment comment = em.persist(Comment.of(firstBook, EXAMPLE_COMMENT));
        assertEquals(comment, repository.findById(comment.getId()).orElse(null));
    }

    @Test
    @DisplayName("Вставка и поиск существующей записи по названию книги")
    void shouldFindByBookName() {
        Comment comment = em.persist(Comment.of(firstBook, EXAMPLE_COMMENT));
        List<Comment> byBookName = repository.findByBookName(comment.getBook().getName());

        assertEquals(1, byBookName.size());
        assertEquals(comment, byBookName.get(0));
    }

    @Test
    @DisplayName("Вставка новой записи")
    void shouldInsertCorrect() {
        Comment comment = repository.save(Comment.of(firstBook, EXAMPLE_COMMENT));
        assertEquals(comment, em.find(Comment.class, comment.getId()));
    }

    @Test
    @DisplayName("Удаление записи")
    void shouldDeleteCorrect() {
        Comment comment = repository.save(Comment.of(firstBook, EXAMPLE_COMMENT));
        assertThat(repository.findById(comment.getId())).get().isEqualTo(comment);
        repository.delete(comment);
        assertThat(repository.findById(comment.getId())).isEmpty();
    }
}