package com.ovgusev.service.impl;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;
import com.ovgusev.repository.BookRepository;
import com.ovgusev.repository.CommentRepository;
import com.ovgusev.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<Book> getBookList() {
        List<Book> books = new ArrayList<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    @Override
    public Book save(String bookName, String author, String genre) {
        return bookRepository.findByName(bookName)
                .map(bookToUpdate -> bookToUpdate.setAuthor(author).setGenre(genre))
                .map(bookRepository::save)
                .orElseGet(() -> bookRepository.save(Book.of(bookName, author, genre)));
    }

    @Override
    public Optional<Book> remove(String bookName) {
        Optional<Book> book = bookRepository.findByName(bookName);
        book.ifPresent(bookRepository::delete);
        return book;
    }

    @Override
    public List<Comment> getCommentList(String bookName) {
        return bookRepository.findByName(bookName)
                .map(book -> commentRepository.findByBookId(book.getId()))
                .orElse(new ArrayList<>());
    }

    @Override
    public Optional<Comment> addComment(String bookName, String commentText) {
        return bookRepository.findByName(bookName)
                .map(book -> {
                    Comment comment = Comment.of(book, commentText);
                    return commentRepository.save(comment);
                });
    }

    @Override
    public Optional<Comment> removeComment(String commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
        return comment;
    }
}