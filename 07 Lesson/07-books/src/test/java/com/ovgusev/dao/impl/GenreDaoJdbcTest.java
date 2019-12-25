package com.ovgusev.dao.impl;

import com.ovgusev.dao.GenreDao;
import com.ovgusev.domain.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import(GenreDaoJdbc.class)
@DisplayName("Testing methods of class GenreDaoJdbc")
class GenreDaoJdbcTest {
    private static final long NOT_EXISTING_ID = -1L;
    private static final String NOT_EXISTING_NAME = "NOT_EXISTING_NAME";
    private static final Genre FIRST_ROW = new Genre(1, "GENRE1");

    @Autowired
    GenreDao dao;

    @Test
    @DisplayName("Поиск несуществующей записи по id")
    void shouldFindByIdEmpty() {
        assertEquals(Optional.empty(), dao.findById(NOT_EXISTING_ID));
    }

    @Test
    @DisplayName("Поиск существующей записи по id")
    void shouldFindByIdCorrect() {
        assertEquals(FIRST_ROW, dao.findById(FIRST_ROW.getId()).get());
    }

    @Test
    @DisplayName("Поиск несуществующей записи по name")
    void shouldFindByNameEmpty() {
        assertEquals(Optional.empty(), dao.findByName(NOT_EXISTING_NAME));
    }

    @Test
    @DisplayName("Поиск существующей записи по name")
    void shouldFindByNameCorrect() {
        assertEquals(FIRST_ROW, dao.findByName(FIRST_ROW.getName()).get());
    }

    @Test
    @DisplayName("Вставка новой записи")
    void shouldInsertCorrect() {
        assertEquals(NOT_EXISTING_NAME, dao.insert(Genre.of(NOT_EXISTING_NAME)).getName());
    }
}