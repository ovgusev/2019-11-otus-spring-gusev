package com.ovgusev.service;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface BookMethodTextService {
    String insertBook(Book bookToInsert, Function<Book, Book> insertingFunction);

    String updateBook(Book bookToUpdate, Function<Book, Book> updatingFunction);

    String deleteBook(Book bookToDelete, Function<Book, Optional<Book>> deletingFunction);

    String getBookListInfo(List<Book> bookList);

    String insertBookComment(Book book, Comment comment);
}
