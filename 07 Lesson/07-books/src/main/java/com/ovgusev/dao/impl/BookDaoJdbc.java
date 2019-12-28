package com.ovgusev.dao.impl;

import com.ovgusev.dao.AuthorDao;
import com.ovgusev.dao.BookDao;
import com.ovgusev.dao.GenreDao;
import com.ovgusev.domain.Author;
import com.ovgusev.domain.Book;
import com.ovgusev.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookDaoJdbc implements BookDao {
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_AUTHOR_ID = "author_id";
    private static final String FIELD_AUTHOR_NAME = "author_name";
    private static final String FIELD_GENRE_ID = "genre_id";
    private static final String FIELD_GENRE_NAME = "genre_name";
    private static final BookMapper BOOK_MAPPER = new BookMapper();

    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final NamedParameterJdbcOperations jdbc;

    private static final String FIND_BOOK_BY_ID_SQL = "select b.id, b.name, b.author_id, a.name as author_name, b.genre_id, g.name as genre_name" +
            " from books b" +
            " join authors a on a.id = b.author_id" +
            " join genres g on g.id = b.genre_id" +
            " where b.id = :id";
    private static final String FIND_BOOK_BY_NAME_SQL = "select b.id, b.name, b.author_id, a.name as author_name, b.genre_id, g.name as genre_name" +
            " from books b" +
            " join authors a on a.id = b.author_id" +
            " join genres g on g.id = b.genre_id" +
            " where b.name = :name";
    private static final String GET_ALL_BOOKS_SQL = "select b.id, b.name, b.author_id, a.name as author_name, b.genre_id, g.name as genre_name" +
            " from books b" +
            " join authors a on a.id = b.author_id" +
            " join genres g on g.id = b.genre_id";

    @Override
    public Book insert(Book book) {
        setDependencies(book);

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(FIELD_NAME, book.getName())
                .addValue(FIELD_AUTHOR_ID, book.getAuthor().getId())
                .addValue(FIELD_GENRE_ID, book.getGenre().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update("insert into books(name, author_id, genre_id) values(:name, :author_id, :genre_id)", sqlParameterSource, keyHolder);

        return book.setId(keyHolder.getKey().longValue());
    }

    @Override
    public Optional<Book> update(Book book) {
        setDependencies(book);

        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(FIELD_ID, book.getId())
                .addValue(FIELD_NAME, book.getName())
                .addValue(FIELD_AUTHOR_ID, book.getAuthor().getId())
                .addValue(FIELD_GENRE_ID, book.getGenre().getId());

        return jdbc.update("update books set name = :name, author_id = :author_id, genre_id = :genre_id where id = :id", sqlParameterSource) == 1 ? Optional.of(book) : Optional.empty();
    }

    @Override
    public Optional<Book> delete(long id) {
        Optional<Book> book = findById(id);
        book.ifPresent(b -> jdbc.update("delete from books where id = :id", Collections.singletonMap(FIELD_ID, b.getId())));
        return book;
    }

    @Override
    public Optional<Book> findById(long id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(FIND_BOOK_BY_ID_SQL, Collections.singletonMap(FIELD_ID, id), BOOK_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> findByName(String name) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(FIND_BOOK_BY_NAME_SQL, Collections.singletonMap(FIELD_NAME, name), BOOK_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query(GET_ALL_BOOKS_SQL, BOOK_MAPPER);
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Book()
                    .setId(resultSet.getLong(FIELD_ID))
                    .setName(resultSet.getString(FIELD_NAME))
                    .setAuthor(new Author(resultSet.getLong(FIELD_AUTHOR_ID), resultSet.getString(FIELD_AUTHOR_NAME)))
                    .setGenre(new Genre(resultSet.getLong(FIELD_GENRE_ID), resultSet.getString(FIELD_GENRE_NAME)));
        }
    }

    private void setDependencies(Book book) {
        String authorName = book.getAuthor().getName();
        book.setAuthor(authorDao.findByName(authorName)
                .orElseGet(() -> authorDao.insert(Author.of(authorName)))
        );

        String genreName = book.getGenre().getName();
        book.setGenre(genreDao.findByName(genreName)
                .orElseGet(() -> genreDao.insert(Genre.of(genreName)))
        );
    }
}
