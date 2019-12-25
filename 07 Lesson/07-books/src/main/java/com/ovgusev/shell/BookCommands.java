package com.ovgusev.shell;

import com.ovgusev.dao.AuthorDao;
import com.ovgusev.dao.BookDao;
import com.ovgusev.dao.GenreDao;
import com.ovgusev.domain.Author;
import com.ovgusev.domain.Book;
import com.ovgusev.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final BookDao bookDao;

    @ShellMethod(value = "List all books", key = {"list-all-books", "list", "all"})
    public String printAllBooks() {
        Map<Genre, List<Book>> booksGroupedByGenres = bookDao.getAll().stream().collect(Collectors.groupingBy(Book::getGenre));
        return booksGroupedByGenres.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getName()))
                .map(e -> e.getKey().getName() + ":\n" + e.getValue().stream()
                        .sorted(Comparator.comparing(Book::getName))
                        .map(Book::getShortInfo)
                        .map(this::tabString)
                        .collect(Collectors.joining("\n")))
                .collect(Collectors.joining("\n"));
    }

    @ShellMethod(value = "Update or insert book", key = {"update-or-insert-book", "ins", "upd", "add", "new"})
    public String updateOrInsertBook(@ShellOption("name") String bookName, @ShellOption("author") String authorName, @ShellOption(value = "genre", defaultValue = "unknown") String genreName) {
        return bookDao.findByName(bookName)
                .map(bookToUpdate ->
                        bookDao.update(bookToUpdate.setAuthor(getAuthor(authorName)).setGenre(getGenre(genreName)))
                                .map(updatedBook -> "Updated book to: " + updatedBook.getShortInfo())
                                .orElse("Unexpected error while updating " + bookToUpdate.getShortInfo())
                )
                .orElse("Inserted book " +
                        bookDao.insert(Book.of(bookName, getAuthor(authorName), getGenre(genreName))).getShortInfo()
                );
    }

    @ShellMethod(value = "Delete book", key = {"delete-book", "del", "rem"})
    public String deleteBook(@ShellOption("name") String bookName) {
        return bookDao.findByName(bookName)
                .map(bookToDelete -> bookDao.delete(bookToDelete.getId())
                        .map(deletedBook -> "Deleted book " + deletedBook.getShortInfo())
                        .orElse("Unexpected error while deleting " + bookToDelete.getShortInfo()))
                .orElse("Book " + bookName + " not found");
    }

    private Author getAuthor(String authorName) {
        return authorDao.findByName(authorName)
                .orElseGet(() -> authorDao.insert(Author.of(authorName)));
    }

    private Genre getGenre(String genreName) {
        return genreDao.findByName(genreName)
                .orElseGet(() -> genreDao.insert(Genre.of(genreName)));
    }

    private String tabString(String s) {
        return "\t" + s;
    }
}
