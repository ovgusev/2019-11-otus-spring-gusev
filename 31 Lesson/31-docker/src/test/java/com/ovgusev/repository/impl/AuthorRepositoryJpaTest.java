package com.ovgusev.repository.impl;

import com.ovgusev.repository.AbstractRepositoryIntegrationTest;
import com.ovgusev.domain.Author;
import com.ovgusev.repository.AuthorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing methods of class AuthorRepositoryJpa")
class AuthorRepositoryJpaTest extends AbstractRepositoryIntegrationTest {
    private static final long NOT_EXISTING_ID = -1L;
    private static final String NOT_EXISTING_NAME = "NOT_EXISTING_NAME";
    private static final Author FIRST_ROW = new Author(1, "AUTHOR1");

    @Autowired
    AuthorRepository repository;

    @Test
    @DisplayName("Поиск несуществующей записи по id")
    void shouldFindByIdEmpty() {
        assertEquals(Optional.empty(), repository.findById(NOT_EXISTING_ID));
    }

    @Test
    @DisplayName("Поиск существующей записи по id")
    void shouldFindByIdCorrect() {
        assertEquals(FIRST_ROW, repository.findById(FIRST_ROW.getId()).orElse(null));
    }

    @Test
    @DisplayName("Поиск несуществующей записи по name")
    void shouldFindByNameEmpty() {
        assertEquals(Optional.empty(), repository.findByName(NOT_EXISTING_NAME));
    }

    @Test
    @DisplayName("Поиск существующей записи по name")
    void shouldFindByNameCorrect() {
        assertEquals(FIRST_ROW, repository.findByName(FIRST_ROW.getName()).orElse(null));
    }

    @Test
    @DisplayName("Вставка новой записи")
    void shouldInsertCorrect() {
        assertEquals(NOT_EXISTING_NAME, repository.save(Author.of(NOT_EXISTING_NAME)).getName());
    }
}