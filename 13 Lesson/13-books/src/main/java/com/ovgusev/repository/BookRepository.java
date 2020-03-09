package com.ovgusev.repository;

import com.ovgusev.domain.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, String> {
    Optional<Book> findByName(String name);
}
