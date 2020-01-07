package com.ovgusev.repository.impl;

import com.ovgusev.domain.Book;
import com.ovgusev.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {
    private final EntityManager entityManager;

    @Override
    public Book insert(Book book) {
        if (entityManager.contains(book)) {
            return entityManager.merge(book);
        } else {
            entityManager.persist(book);
            return book;
        }
    }

    @Override
    public Optional<Book> update(Book book) {
        if (entityManager.contains(book)) {
            return Optional.of(entityManager.merge(book));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> delete(long id) {
        Optional<Book> bookById = findById(id);

        bookById.ifPresent(entityManager::remove);

        return bookById;
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public Optional<Book> findByName(String name) {
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b where name = :name", Book.class);

        query.setParameter("name", name);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b", Book.class);
        return query.getResultList();
    }
}
