package com.ovgusev.service;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;

import java.util.List;

public interface BookService {
    Book findById(long id);

    List<Book> getBookList();

    void edit(long id, String bookName, String authorName, String genreName);

    void remove(long id);

    List<Comment> getCommentList(long bookId);

    void addComment(long bookId, String commentText);

    void removeComment(long commentId);
}
