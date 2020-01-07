package com.ovgusev.service.impl;

import com.ovgusev.domain.Book;
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
    @Override
    public String insertBook(Book insertedBook) {
        return "Inserted book " + getShortInfo(insertedBook);
    }

    @Override
    public String updateBook(Book bookToUpdate, Function<Book, Optional<Book>> updatingFunction) {
        return updatingFunction.apply(bookToUpdate)
                .map(updatedBook -> "Updated book to: " + getShortInfo(updatedBook))
                .orElse("Unexpected error while updating " + getShortInfo(bookToUpdate));
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
                .map(e -> e.getKey().getName() + ":\n" + e.getValue().stream()
                        .sorted(Comparator.comparing(Book::getName))
                        .map(this::getShortInfo)
                        .map(this::tabString)
                        .collect(Collectors.joining("\n")))
                .collect(Collectors.joining("\n"));
    }

    private String getShortInfo(Book book) {
        return book.getName() + " by " + book.getAuthor().getName();
    }

    private String tabString(String s) {
        return "\t" + s;
    }
}
