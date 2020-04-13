package com.ovgusev.service;

import com.ovgusev.domain.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getCommentList(long bookId);

    void addComment(long bookId, String commentText);

    void removeComment(long commentId);
}
