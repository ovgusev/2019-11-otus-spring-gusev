package com.ovgusev.repository;

import com.ovgusev.domain.Author;

import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(long id);

    Optional<Author> findByName(String name);

    Author save(Author author);
}
