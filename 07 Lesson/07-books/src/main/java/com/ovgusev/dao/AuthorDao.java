package com.ovgusev.dao;

import com.ovgusev.domain.Author;

import java.util.Optional;

public interface AuthorDao {
    Optional<Author> findById(long id);

    Optional<Author> findByName(String name);

    Author insert(Author author);
}
