package com.ovgusev.repository.impl;

import com.ovgusev.domain.Book;
import com.ovgusev.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@DisplayName("Testing methods of class BookRepository")
class BookRepositoryTest {
    @Autowired
    private BookRepository repository;

    private static final String NOT_EXISTING_ID = "NOT_EXISTING_ID";
    private static final String NOT_EXISTING_NAME = "NOT_EXISTING_NAME";
    private static final Book FIRST_BOOK = Book.of("Test book", "Test author", "Test genre");

    @BeforeEach
    void init() {
        repository.deleteAll();
        repository.save(FIRST_BOOK);
    }

    @Test
    @DisplayName("Поиск несуществующей записи по id")
    void shouldFindByIdEmpty() {
        assertEquals(Optional.empty(), repository.findById(NOT_EXISTING_ID));
    }

    @Test
    @DisplayName("Поиск существующей записи по id")
    void shouldFindByIdCorrect() {
        assertEquals(FIRST_BOOK, repository.findById(FIRST_BOOK.getId()).orElse(null));
    }

    @Test
    @DisplayName("Поиск несуществующей записи по name")
    void shouldFindByNameEmpty() {
        assertEquals(Optional.empty(), repository.findByName(NOT_EXISTING_NAME));
    }

    @Test
    @DisplayName("Поиск существующей записи по name")
    void shouldFindByNameCorrect() {
        assertEquals(FIRST_BOOK, repository.findByName(FIRST_BOOK.getName()).orElse(null));
    }

    @Test
    @DisplayName("Вставка новой записи")
    void shouldInsertCorrect() {
        assertEquals(NOT_EXISTING_NAME, repository.save(Book.of(NOT_EXISTING_NAME, FIRST_BOOK.getAuthor(), FIRST_BOOK.getGenre())).getName());
    }

    @Test
    @DisplayName("Модификация поля name")
    void shouldUpdateCorrect() {
        Book book = repository.findByName(FIRST_BOOK.getName()).orElseGet(null);

        assertThat(repository.save(book.setName(NOT_EXISTING_NAME)))
                .hasFieldOrPropertyWithValue("name", NOT_EXISTING_NAME);

        assertEquals(NOT_EXISTING_NAME, repository.findById(FIRST_BOOK.getId()).map(Book::getName).orElse(null));
    }

    @Test
    @DisplayName("Удаление записи")
    void shouldDeleteCorrect() {
        assertThat(FIRST_BOOK).isEqualTo(repository.findById(FIRST_BOOK.getId()).orElseGet(null));
        repository.delete(FIRST_BOOK);
        assertThat(repository.findById(FIRST_BOOK.getId())).isEmpty();
    }

    @Test
    @DisplayName("Проверка получения всех записей")
    void shouldFindAll() {
        Collection<Book> all = (Collection<Book>) repository.findAll();

        assertEquals(1, all.size());
        assertTrue(all.contains(FIRST_BOOK));
    }
}