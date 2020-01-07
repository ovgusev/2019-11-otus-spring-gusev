package com.ovgusev.shell;

import com.ovgusev.domain.Author;
import com.ovgusev.domain.Book;
import com.ovgusev.domain.Genre;
import com.ovgusev.repository.BookRepository;
import com.ovgusev.service.BookMethodTextService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {
    private final BookMethodTextService bookMethodTextService;
    private final BookRepository bookRepository;

    @ShellMethod(value = "List all books", key = {"list-all-books", "list", "all"})
    public String printAllBooks() {
        return bookMethodTextService.getBookListInfo(bookRepository.getAll());
    }

    @ShellMethod(value = "Update or insert book", key = {"update-or-insert-book", "ins", "upd", "add", "new"})
    public String updateOrInsertBook(@ShellOption("name") String bookName, @ShellOption("author") String authorName, @ShellOption(value = "genre", defaultValue = "unknown") String genreName) {
        return bookRepository.findByName(bookName)
                .map(bookToUpdate -> bookToUpdate.setAuthor(Author.of(authorName)).setGenre(Genre.of(genreName)))
                .map(bookToUpdate -> bookMethodTextService.updateBook(bookToUpdate, bookRepository::update))
                .orElseGet(() -> bookMethodTextService.insertBook(bookRepository.insert(Book.of(bookName, Author.of(authorName), Genre.of(genreName)))));
    }

    @ShellMethod(value = "Delete book", key = {"delete-book", "del", "rem"})
    public String deleteBook(@ShellOption("name") String bookName) {
        return bookRepository.findByName(bookName)
                .map(bookToDelete -> bookMethodTextService.deleteBook(bookToDelete, book -> bookRepository.delete(book.getId())))
                .orElseGet(() -> "Book " + bookName + " not found");
    }
}
