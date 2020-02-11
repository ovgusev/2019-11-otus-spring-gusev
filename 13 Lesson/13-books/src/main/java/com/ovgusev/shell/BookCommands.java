package com.ovgusev.shell;

import com.ovgusev.service.BookMethodTextService;
import com.ovgusev.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class BookCommands {
    private final BookMethodTextService bookMethodTextService;
    private final BookService bookService;

    @ShellMethod(value = "List all books", key = {"list-all-books", "list", "all"})
    public String getBookList() {
        return bookMethodTextService.getBookListText(bookService.getBookList());
    }

    @ShellMethod(value = "Update or insert book", key = {"update-or-insert-book", "ins", "upd", "add", "new", "save"})
    public String saveBook(@ShellOption("book") String bookName, @ShellOption("author") String authorName, @ShellOption(value = "genre", defaultValue = "unknown") String genreName) {
        return bookMethodTextService.saveBookText(bookService.save(bookName, authorName, genreName));
    }

    @ShellMethod(value = "Delete book", key = {"delete-book", "del", "rem"})
    public String removeBook(@ShellOption("bookName") String bookName) {
        return bookMethodTextService.removeBookText(bookName, bookService.remove(bookName));
    }

    @ShellMethod(value = "Show book comment list", key = {"comments-list", "cl"})
    public String getCommentList(@ShellOption("book") String bookName) {
        return bookMethodTextService.getCommentListText(bookService.getCommentList(bookName));
    }

    @ShellMethod(value = "Add comment", key = {"comment-add", "ca"})
    public String addComment(@ShellOption("book") String bookName, @ShellOption("comment") String commentText) {
        return bookMethodTextService.addCommentText(bookName, bookService.addComment(bookName, commentText));
    }

    @ShellMethod(value = "Remove comment", key = {"comment-remove", "cr"})
    public String removeComment(@ShellOption("commentId") long commentId) {
        return bookMethodTextService.removeCommentText(commentId, bookService.removeComment(commentId));
    }
}
