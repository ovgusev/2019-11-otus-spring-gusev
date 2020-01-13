package com.ovgusev.repository.impl;

import com.ovgusev.domain.Author;
import com.ovgusev.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class AuthorRepositoryJpa implements AuthorRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }

    @Override
    public Optional<Author> findByName(String name) {
        TypedQuery<Author> query = entityManager.createQuery("select a from Author a where name = :name", Author.class);

        query.setParameter("name", name);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Author save(Author author) {
        if (author.getId() > 0) {
            return entityManager.merge(author);
        } else {
            entityManager.persist(author);
            return author;
        }
    }
}
