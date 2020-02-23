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
import java.util.List;
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
    public Book findById(long id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    @Override
    public List<Book> getBookList() {
        return bookRepository.findAll();
    }

    @Override
    public void edit(long id, String bookName, String authorName, String genreName) {
        Author author = authorRepository.findByName(authorName).orElseGet(() -> Author.of(authorName));
        Genre genre = genreRepository.findByName(genreName).orElseGet(() -> Genre.of(genreName));

        Book book = bookRepository.findById(id).orElseGet(Book::new)
                .setName(bookName)
                .setAuthor(author)
                .setGenre(genre);

        bookRepository.save(book);
    }

    @Override
    public void remove(long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Comment> getCommentList(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public void addComment(long bookId, String commentText) {
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);

        Comment comment = Comment.of(book, commentText);
        commentRepository.save(comment);
    }

    @Override
    public void removeComment(long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }
}