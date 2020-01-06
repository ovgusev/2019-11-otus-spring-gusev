package com.ovgusev.dao;

import com.ovgusev.domain.Genre;

import java.util.Optional;

public interface GenreDao {
    Optional<Genre> findById(long id);

    Optional<Genre> findByName(String name);

    Genre insert(Genre genre);
}
