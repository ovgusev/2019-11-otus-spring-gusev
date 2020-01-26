package com.ovgusev.repository;

import com.ovgusev.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(long id);

    List<Comment> findByBookName(String bookName);

    Comment save(Comment comment);

    Optional<Comment> remove(Comment comment);
}
