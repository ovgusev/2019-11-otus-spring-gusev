package com.ovgusev.repository;

import com.ovgusev.domain.Genre;

import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(long id);

    Optional<Genre> findByName(String name);

    Genre insert(Genre genre);
}
