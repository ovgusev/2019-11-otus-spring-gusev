package com.ovgusev.service;

import com.ovgusev.domain.Book;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface BookMethodTextService {
    String insertBook(Book insertedBook);

    String updateBook(Book bookToUpdate, Function<Book, Optional<Book>> updatingFunction);

    String deleteBook(Book bookToDelete, Function<Book, Optional<Book>> deletingFunction);

    String getBookListInfo(List<Book> bookList);
}
