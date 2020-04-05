package com.ovgusev.repository.impl;

import com.ovgusev.AbstractRepositoryIntegrationTest;
import com.ovgusev.domain.Genre;
import com.ovgusev.repository.GenreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Testing methods of class GenreRepositoryJpa")
class GenreRepositoryJpaTest extends AbstractRepositoryIntegrationTest {
    private static final long NOT_EXISTING_ID = -1L;
    private static final String NOT_EXISTING_NAME = "NOT_EXISTING_NAME";
    private static final Genre FIRST_ROW = new Genre(1, "GENRE1");

    @Autowired
    GenreRepository repository;

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
        assertEquals(NOT_EXISTING_NAME, repository.save(Genre.of(NOT_EXISTING_NAME)).getName());
    }
}