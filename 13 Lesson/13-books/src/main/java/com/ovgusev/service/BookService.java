package com.ovgusev.service;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getBookList();

    Book save(String bookName, String author, String genre);

    Optional<Book> remove(String bookName);

    List<Comment> getCommentList(String bookName);

    Optional<Comment> addComment(String bookName, String commentText);

    Optional<Comment> removeComment(String commentId);
}
