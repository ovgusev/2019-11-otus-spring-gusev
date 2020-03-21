package com.ovgusev.service.impl;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;
import com.ovgusev.service.BookMethodTextService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookMethodTextServiceImpl implements BookMethodTextService {
    private static String CRLF = System.getProperty("line.separator");

    @Override
    public String saveBookText(Book book) {
        return "Modified book: " + getShortInfo(book);
    }

    @Override
    public String removeBookText(String bookName, Optional<Book> book) {
        return book
                .map(b -> "Removed book " + getShortInfo(b))
                .orElseGet(() -> bookNotFoundText(bookName));
    }

    @Override
    public String getCommentListText(List<Comment> commentList) {
        return commentList
                .stream()
                .map(Comment::toString)
                .collect(Collectors.joining(CRLF));
    }

    @Override
    public String getBookListText(List<Book> bookList) {
        Map<String, List<Book>> booksGroupedByGenres = bookList.stream().collect(Collectors.groupingBy(Book::getGenre));
        return booksGroupedByGenres.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + ":" + CRLF + e.getValue().stream()
                        .sorted(Comparator.comparing(Book::getName))
                        .map(this::getShortInfo)
                        .map(this::tabString)
                        .collect(Collectors.joining(CRLF)))
                .collect(Collectors.joining(CRLF));
    }

    @Override
    public String addCommentText(String bookName, Optional<Map.Entry<Book, Comment>> bookCommentEntry) {
        return bookCommentEntry
                .map(e -> "Added comment for book " + getShortInfo(e.getKey()) + CRLF + e.getValue().getText())
                .orElseGet(() -> bookNotFoundText(bookName));
    }

    @Override
    public String removeCommentText(String commentId, Optional<Comment> comment) {
        return comment
                .map(c -> "Removed comment " + c.toString())
                .orElseGet(() -> "Comment " + commentId + " not found");
    }

    private String getShortInfo(Book book) {
        return book.getName() + " by " + book.getAuthor();
    }

    private String tabString(String s) {
        return "\t" + s;
    }

    private static String bookNotFoundText(String bookName) {
        return "Book " + bookName + " not found";
    }
}
