package com.ovgusev.dao.impl;

import com.ovgusev.dao.AuthorDao;
import com.ovgusev.dao.BookDao;
import com.ovgusev.dao.GenreDao;
import com.ovgusev.domain.Book;
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
public class BookDaoJdbc implements BookDao {
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_AUTHOR_ID = "author_id";
    private static final String FIELD_GENRE_ID = "genre_id";
    private final NamedParameterJdbcOperations jdbc;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookMapper bookMapper;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbc, AuthorDao authorDao, GenreDao genreDao) {
        this.jdbc = jdbc;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
        this.bookMapper = new BookMapper(authorDao, genreDao);
    }

    @Override
    public Book insert(Book book) {
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
            return Optional.ofNullable(jdbc.queryForObject("select * from books where id = :id", Collections.singletonMap(FIELD_ID, id), bookMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> findByName(String name) {
        try {
            return Optional.ofNullable(jdbc.queryForObject("select * from books where name = :name", Collections.singletonMap(FIELD_NAME, name), bookMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select * from books", bookMapper);
    }

    private static class BookMapper implements RowMapper<Book> {
        private final AuthorDao authorDao;
        private final GenreDao genreDao;

        public BookMapper(AuthorDao authorDao, GenreDao genreDao) {
            this.authorDao = authorDao;
            this.genreDao = genreDao;
        }

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Book()
                    .setId(resultSet.getLong(FIELD_ID))
                    .setName(resultSet.getString(FIELD_NAME))
                    .setAuthor(authorDao.findById(resultSet.getLong(FIELD_AUTHOR_ID)).get())
                    .setGenre(genreDao.findById(resultSet.getLong(FIELD_GENRE_ID)).get());
        }
    }
}
