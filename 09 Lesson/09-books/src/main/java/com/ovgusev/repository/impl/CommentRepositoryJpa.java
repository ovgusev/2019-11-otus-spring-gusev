package com.ovgusev.repository.impl;

import com.ovgusev.domain.Comment;
import com.ovgusev.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = Transactional.TxType.MANDATORY)
@RequiredArgsConstructor
public class CommentRepositoryJpa implements CommentRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Override
    public List<Comment> findByBookName(String bookName) {
        TypedQuery<Comment> query = entityManager.createQuery("select c from Comment c where c.book.name = :book_name", Comment.class);

        query.setParameter("book_name", bookName);

        return query.getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() > 0) {
            return entityManager.merge(comment);
        } else {
            entityManager.persist(comment);
            return comment;
        }
    }

    @Override
    public Optional<Comment> remove(Comment comment) {
        return Optional.ofNullable(comment)
                .map(c -> entityManager.contains(c) ? c : findById(c.getId()).orElse(null))
                .map(c -> {
                    entityManager.remove(c);
                    return c;
                });
    }
}
