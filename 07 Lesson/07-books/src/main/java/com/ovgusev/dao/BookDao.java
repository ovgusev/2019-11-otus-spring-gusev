package com.ovgusev.dao;

import com.ovgusev.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book insert(Book book);

    Optional<Book> update(Book book);

    Optional<Book> delete(long id);

    Optional<Book> findById(long id);

    Optional<Book> findByName(String name);

    List<Book> getAll();
}
