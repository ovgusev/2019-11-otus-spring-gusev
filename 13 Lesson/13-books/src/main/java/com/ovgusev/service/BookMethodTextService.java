package com.ovgusev.service;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface BookMethodTextService {
    String getBookListText(List<Book> bookList);

    String saveBookText(Book book);

    String removeBookText(String bookName, Optional<Book> book);

    String getCommentListText(List<Comment> commentList);

    String addCommentText(String bookName, Optional<Comment> bookCommentEntry);

    String removeCommentText(String commentId, Optional<Comment> comment);
}
