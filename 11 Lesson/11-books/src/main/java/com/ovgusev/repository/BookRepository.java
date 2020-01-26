package com.ovgusev.repository;

import com.ovgusev.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> remove(Book book);

    Optional<Book> findById(long id);

    Optional<Book> findByName(String name);

    List<Book> getAll();
}
