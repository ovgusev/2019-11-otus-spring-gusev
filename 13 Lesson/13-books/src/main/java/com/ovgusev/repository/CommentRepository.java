package com.ovgusev.repository;

import com.ovgusev.domain.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, String> {
    List<Comment> findByBookId(String bookId);
}
