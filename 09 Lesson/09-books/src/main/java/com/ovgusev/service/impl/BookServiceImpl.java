package com.ovgusev.service.impl;

import com.ovgusev.domain.Author;
import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;
import com.ovgusev.domain.Genre;
import com.ovgusev.repository.AuthorRepository;
import com.ovgusev.repository.BookRepository;
import com.ovgusev.repository.CommentRepository;
import com.ovgusev.repository.GenreRepository;
import com.ovgusev.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<Book> getBookList() {
        return bookRepository.getAll();
    }

    @Override
    public Book save(String bookName, String authorName, String genreName) {
        Author author = authorRepository.findByName(authorName).orElseGet(() -> Author.of(authorName));
        Genre genre = genreRepository.findByName(genreName).orElseGet(() -> Genre.of(genreName));

        return bookRepository.findByName(bookName)
                .map(bookToUpdate -> bookToUpdate.setAuthor(author).setGenre(genre))
                .map(bookRepository::save)
                .orElseGet(() -> bookRepository.save(Book.of(bookName, author, genre)));
    }

    @Override
    public Optional<Book> remove(String bookName) {
        return bookRepository.findByName(bookName)
                .map(bookRepository::remove)
                .flatMap(book -> book);
    }

    @Override
    public List<Comment> getCommentList(String bookName) {
        return commentRepository.findByBookName(bookName);
    }

    @Override
    public Optional<Map.Entry<Book, Comment>> addComment(String bookName, String commentText) {
        return bookRepository.findByName(bookName)
                .map(book -> {
                    Comment comment = Comment.of(book, commentText);
                    commentRepository.save(comment);
                    return new AbstractMap.SimpleImmutableEntry<>(book, comment);
                });
    }

    @Override
    public Optional<Comment> removeComment(long commentId) {
        return commentRepository.findById(commentId)
                .map(commentRepository::remove)
                .flatMap(comment -> comment);
    }
}