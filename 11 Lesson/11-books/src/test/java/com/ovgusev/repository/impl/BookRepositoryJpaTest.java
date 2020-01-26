package com.ovgusev.repository.impl;

import com.ovgusev.domain.Book;
import com.ovgusev.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DisplayName("Testing methods of class BookRepositoryJpa")
class BookRepositoryJpaTest {
    @Autowired
    private TestEntityManager em;

    private static final long NOT_EXISTING_ID = -1L;
    private static final String NOT_EXISTING_NAME = "NOT_EXISTING_NAME";
    private Book firstRow;

    @BeforeEach
    void init() {
        firstRow = em.find(Book.class, 1L);
    }

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("Поиск несуществующей записи по id")
    void shouldFindByIdEmpty() {
        assertEquals(Optional.empty(), repository.findById(NOT_EXISTING_ID));
    }

    @Test
    @DisplayName("Поиск существующей записи по id")
    void shouldFindByIdCorrect() {
        assertEquals(firstRow, repository.findById(firstRow.getId()).orElse(null));
    }

    @Test
    @DisplayName("Поиск несуществующей записи по name")
    void shouldFindByNameEmpty() {
        assertEquals(Optional.empty(), repository.findByName(NOT_EXISTING_NAME));
    }

    @Test
    @DisplayName("Поиск существующей записи по name")
    void shouldFindByNameCorrect() {
        assertEquals(firstRow, repository.findByName(firstRow.getName()).orElse(null));
    }

    @Test
    @DisplayName("Вставка новой записи")
    void shouldInsertCorrect() {
        assertEquals(NOT_EXISTING_NAME, repository.save(Book.of(NOT_EXISTING_NAME, firstRow.getAuthor(), firstRow.getGenre())).getName());
    }

    @Test
    @DisplayName("Модификация поля name")
    void shouldUpdateCorrect() {
        assertThat(repository.save(firstRow.setName(NOT_EXISTING_NAME)))
                .hasFieldOrPropertyWithValue("name", NOT_EXISTING_NAME);

        assertEquals(NOT_EXISTING_NAME, repository.findById(firstRow.getId()).map(Book::getName).orElse(null));
    }

    @Test
    @DisplayName("Удаление записи")
    void shouldDeleteCorrect() {
        assertThat(firstRow).isEqualTo(repository.findById(firstRow.getId()).orElseGet(null));
        repository.delete(firstRow);
        assertThat(repository.findById(firstRow.getId())).isEmpty();
    }

    @Test
    @DisplayName("Проверка получения всех записей")
    void shouldFindAll() {
        List<Book> all = repository.findAll();

        assertEquals(1, all.size());
        assertTrue(all.contains(firstRow));
    }
}