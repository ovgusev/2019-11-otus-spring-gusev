package com.ovgusev.repository;

import com.ovgusev.domain.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(value = "Book.allAttributes")
    List<Book> findAll();

    @EntityGraph(value = "Book.allAttributes")
    Optional<Book> findById(Long id);

    @EntityGraph(value = "Book.allAttributes")
    Optional<Book> findByName(String name);
}
