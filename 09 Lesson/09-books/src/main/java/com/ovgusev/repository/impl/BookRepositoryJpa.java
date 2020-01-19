package com.ovgusev.repository.impl;

import com.ovgusev.domain.Book;
import com.ovgusev.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = Transactional.TxType.MANDATORY)
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Book save(Book book) {
        if (book.getId() > 0) {
            return entityManager.merge(book);
        } else {
            entityManager.persist(book);
            return book;
        }
    }

    @Override
    public Optional<Book> remove(Book book) {
        return Optional.ofNullable(book)
                .map(b -> entityManager.contains(b) ? b : findById(b.getId()).orElse(null))
                .map(b -> {
                    entityManager.remove(b);
                    return b;
                });
    }

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public Optional<Book> findByName(String name) {
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b join fetch b.author join fetch b.genre where b.name = :name", Book.class);

        query.setParameter("name", name);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b join fetch b.author join fetch b.genre", Book.class);
        return query.getResultList();
    }
}
