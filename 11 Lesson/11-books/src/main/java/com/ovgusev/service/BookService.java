package com.ovgusev.service;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookService {
    List<Book> getBookList();

    Book save(String bookName, String authorName, String genreName);

    Optional<Book> remove(String bookName);

    List<Comment> getCommentList(String bookName);

    Optional<Map.Entry<Book, Comment>> addComment(String bookName, String commentText);

    Optional<Comment> removeComment(long commentId);
}
