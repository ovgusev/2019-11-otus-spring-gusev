package com.ovgusev.dao.impl;

import com.ovgusev.dao.GenreDao;
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
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreDaoJdbc implements GenreDao {
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final GenreMapper GENRE_MAPPER = new GenreMapper();
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Genre> findById(long id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject("select * from genres where id = :id", Collections.singletonMap(FIELD_ID, id), GENRE_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Genre> findByName(String name) {
        try {
            return Optional.ofNullable(jdbc.queryForObject("select * from genres where name = :name", Collections.singletonMap(FIELD_NAME, name), GENRE_MAPPER));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Genre insert(Genre genre) {
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue(FIELD_NAME, genre.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update("insert into genres(name) values(:name)", sqlParameterSource, keyHolder);

        return genre.setId(keyHolder.getKey().longValue());
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Genre(resultSet.getLong(FIELD_ID), resultSet.getString(FIELD_NAME));
        }
    }
}
