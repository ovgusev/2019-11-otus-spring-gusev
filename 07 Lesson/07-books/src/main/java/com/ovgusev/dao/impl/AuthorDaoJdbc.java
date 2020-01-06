package com.ovgusev.dao.impl;

import com.ovgusev.dao.AuthorDao;
import com.ovgusev.domain.Author;
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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorDaoJdbc implements AuthorDao {
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final AuthorMapper AUTHOR_MAPPER = new AuthorMapper();
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Author> findById(long id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject("select * from authors where id = :id", Collections.singletonMap(FIELD_ID, id), AUTHOR_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Author> findByName(String name) {
        try {
            return Optional.ofNullable(jdbc.queryForObject("select * from authors where name = :name", Collections.singletonMap(FIELD_NAME, name), AUTHOR_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Author insert(Author author) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(FIELD_NAME, author.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update("insert into authors(name) values(:name)", sqlParameterSource, keyHolder);

        return author.setId(keyHolder.getKey().longValue());
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Author(resultSet.getLong(FIELD_ID), resultSet.getString(FIELD_NAME));
        }
    }
}
