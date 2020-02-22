package com.ovgusev.service.impl;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;
import com.ovgusev.repository.BookRepository;
import com.ovgusev.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
//@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
//    private final CommentRepository commentRepository;

    @Override
    public List<Book> getBookList() {
        return bookRepository.findAll();
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
        return null;
//        commentRepository.findByBookName(bookName);
    }

    @Override
    public Optional<Map.Entry<Book, Comment>> addComment(String bookName, String commentText) {
        return null;
/*        return bookRepository.findByName(bookName)
                .map(book -> {
                    Comment comment = Comment.of(book, commentText);
                    commentRepository.save(comment);
                    return new AbstractMap.SimpleImmutableEntry<>(book, comment);
                });*/
    }

    @Override
    public Optional<Comment> removeComment(long commentId) {
        return null;
/*        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
        return comment;*/
    }
}