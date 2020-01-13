package com.ovgusev.shell;

import com.ovgusev.domain.Author;
import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;
import com.ovgusev.domain.Genre;
import com.ovgusev.repository.AuthorRepository;
import com.ovgusev.repository.BookRepository;
import com.ovgusev.repository.CommentRepository;
import com.ovgusev.repository.GenreRepository;
import com.ovgusev.service.BookMethodTextService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

import static com.ovgusev.service.impl.BookMethodTextServiceImpl.CRLF;

@ShellComponent
@Transactional
@RequiredArgsConstructor
public class BookCommands {
    private final BookMethodTextService bookMethodTextService;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @ShellMethod(value = "List all books", key = {"list-all-books", "list", "all"})
    public String printAllBooks() {
        return bookMethodTextService.getBookListInfo(bookRepository.getAll());
    }

    @ShellMethod(value = "Update or insert book", key = {"update-or-insert-book", "ins", "upd", "add", "new"})
    public String updateOrInsertBook(@ShellOption("book") String bookName, @ShellOption("author") String authorName, @ShellOption(value = "genre", defaultValue = "unknown") String genreName) {
        Author author = authorRepository.findByName(authorName).orElseGet(() -> Author.of(authorName));
        Genre genre = genreRepository.findByName(genreName).orElseGet(() -> Genre.of(genreName));

        return bookRepository.findByName(bookName)
                .map(bookToUpdate -> bookToUpdate.setAuthor(author).setGenre(genre))
                .map(bookToUpdate -> bookMethodTextService.updateBook(bookToUpdate, bookRepository::save))
                .orElseGet(() -> bookMethodTextService.insertBook(Book.of(bookName, author, genre), bookRepository::save));
    }

    @ShellMethod(value = "Delete book", key = {"delete-book", "del", "rem"})
    public String deleteBook(@ShellOption("bookName") String bookName) {
        return bookRepository.findByName(bookName)
                .map(bookToDelete -> bookMethodTextService.deleteBook(bookToDelete, bookRepository::remove))
                .orElseGet(() -> bookNotFoundText(bookName));
    }

    @ShellMethod(value = "Show book comment list", key = {"comments-list", "cl"})
    public String showComments(@ShellOption("book") String bookName) {
        return commentRepository.findByBookName(bookName).stream()
                .map(Comment::toString)
                .collect(Collectors.joining(CRLF));
    }

    @ShellMethod(value = "Add comment", key = {"comment-add", "ca"})
    public String addComment(@ShellOption("book") String bookName, @ShellOption("comment") String commentText) {
        return bookRepository.findByName(bookName)
                .map(book -> {
                    Comment comment = Comment.of(book, commentText);
                    commentRepository.save(comment);
                    return bookMethodTextService.insertBookComment(book, comment);
                })
                .orElseGet(() -> bookNotFoundText(bookName));
    }

    @ShellMethod(value = "Remove comment", key = {"comment-remove", "cr"})
    public String removeComment(@ShellOption("commentId") long commentId) {
        return commentRepository.findById(commentId)
                .map(commentRepository::remove)
                .map(comment -> "Removed comment " + comment.toString())
                .orElseGet(() -> "Comment " + commentId + " not found");
    }

    private static String bookNotFoundText(String bookName) {
        return "Book " + bookName + " not found";
    }
}
