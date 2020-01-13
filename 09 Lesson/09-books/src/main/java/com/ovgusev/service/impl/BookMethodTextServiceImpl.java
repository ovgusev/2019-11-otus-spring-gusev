package com.ovgusev.service.impl;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;
import com.ovgusev.domain.Genre;
import com.ovgusev.service.BookMethodTextService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookMethodTextServiceImpl implements BookMethodTextService {
    public static String CRLF = System.getProperty("line.separator");

    @Override
    public String insertBook(Book bookToInsert, Function<Book, Book> insertingFunction) {
        insertingFunction.apply(bookToInsert);
        return "Inserted book " + getShortInfo(bookToInsert);
    }

    @Override
    public String updateBook(Book bookToUpdate, Function<Book, Book> updatingFunction) {
        updatingFunction.apply(bookToUpdate);
        return "Updated book to: " + getShortInfo(bookToUpdate);
    }

    @Override
    public String deleteBook(Book bookToDelete, Function<Book, Optional<Book>> deletingFunction) {
        return deletingFunction.apply(bookToDelete)
                .map(deletedBook -> "Deleted book " + getShortInfo(deletedBook))
                .orElse("Unexpected error while deleting " + getShortInfo(bookToDelete));
    }

    @Override
    public String getBookListInfo(List<Book> bookList) {
        Map<Genre, List<Book>> booksGroupedByGenres = bookList.stream().collect(Collectors.groupingBy(Book::getGenre));
        return booksGroupedByGenres.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getName()))
                .map(e -> e.getKey().getName() + ":" + CRLF + e.getValue().stream()
                        .sorted(Comparator.comparing(Book::getName))
                        .map(this::getShortInfo)
                        .map(this::tabString)
                        .collect(Collectors.joining(CRLF)))
                .collect(Collectors.joining(CRLF));
    }

    @Override
    public String insertBookComment(Book book, Comment comment) {
        return "Added comment for book " + getShortInfo(book) + System.getProperty("line.separator") + comment.getText();
    }

    private String getShortInfo(Book book) {
        return book.getName() + " by " + book.getAuthor().getName();
    }

    private String tabString(String s) {
        return "\t" + s;
    }
}
