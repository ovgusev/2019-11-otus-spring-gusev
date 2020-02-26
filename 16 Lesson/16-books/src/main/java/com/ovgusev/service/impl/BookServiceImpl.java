package com.ovgusev.service.impl;

import com.ovgusev.domain.Author;
import com.ovgusev.domain.Book;
import com.ovgusev.domain.Genre;
import com.ovgusev.repository.AuthorRepository;
import com.ovgusev.repository.BookRepository;
import com.ovgusev.repository.GenreRepository;
import com.ovgusev.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

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
}