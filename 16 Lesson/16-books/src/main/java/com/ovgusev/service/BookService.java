package com.ovgusev.service;

import com.ovgusev.domain.Book;

import java.util.List;

public interface BookService {
    Book findById(long id);

    List<Book> getBookList();

    void edit(long id, String bookName, String authorName, String genreName);

    void remove(long id);
}
