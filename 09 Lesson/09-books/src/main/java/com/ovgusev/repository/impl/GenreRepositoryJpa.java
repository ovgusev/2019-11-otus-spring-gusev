package com.ovgusev.repository.impl;

import com.ovgusev.domain.Genre;
import com.ovgusev.repository.GenreRepository;
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
public class GenreRepositoryJpa implements GenreRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Genre> findById(long id) {
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }

    @Override
    public Optional<Genre> findByName(String name) {
        TypedQuery<Genre> query = entityManager.createQuery("select g from Genre g where name = :name", Genre.class);

        query.setParameter("name", name);

        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() > 0) {
            return entityManager.merge(genre);
        } else {
            entityManager.persist(genre);
            return genre;
        }
    }
}
