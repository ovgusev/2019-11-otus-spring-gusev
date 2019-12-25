package com.ovgusev.dao.impl;

import com.ovgusev.dao.BookDao;
import com.ovgusev.domain.Author;
import com.ovgusev.domain.Book;
import com.ovgusev.domain.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@Import({BookDaoJdbc.class, AuthorDaoJdbc.class, GenreDaoJdbc.class})
@DisplayName("Testing methods of class BookDaoJdbc")
class BookDaoJdbcTest {
    private static final long NOT_EXISTING_ID = -1L;
    private static final String NOT_EXISTING_NAME = "NOT_EXISTING_NAME";
    private static final Book FIRST_ROW = new Book(1, "BOOK1", new Author(1, "AUTHOR1"), new Genre(1, "GENRE1"));

    @Autowired
    BookDao dao;

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
        assertEquals(NOT_EXISTING_NAME, dao.insert(Book.of(NOT_EXISTING_NAME, FIRST_ROW.getAuthor(), FIRST_ROW.getGenre())).getName());
    }

    @Test
    @DisplayName("Модификация поля name")
    void shouldUpdateCorrect() {
        assertEquals(NOT_EXISTING_NAME, dao.update(
                Book.of(NOT_EXISTING_NAME, FIRST_ROW.getAuthor(), FIRST_ROW.getGenre()).setId(FIRST_ROW.getId()))
                .get().getName()
        );

        assertEquals(NOT_EXISTING_NAME, dao.findById(FIRST_ROW.getId()).get().getName());
    }

    @Test
    @DisplayName("Удаление записи")
    void delete() {
        assertEquals(FIRST_ROW, dao.delete(FIRST_ROW.getId()).get());
        assertEquals(Optional.empty(), dao.delete(FIRST_ROW.getId()));
    }

    @Test
    @DisplayName("Проверка получения всех записей")
    void shouldGetAll() {
        List<Book> all = dao.getAll();

        assertEquals(1, all.size());
        assertTrue(all.contains(FIRST_ROW));
    }
}