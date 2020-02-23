package com.ovgusev.controller;

import com.ovgusev.domain.Author;
import com.ovgusev.domain.Book;
import com.ovgusev.domain.Genre;
import com.ovgusev.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class BookController {
    public static final String BOOK_LIST_URL = "/";
    public static final String BOOK_EDIT_URL = "/edit";
    public static final String COMMENT_LIST_URL = "/comment-list";
    public static final String COMMENT_ADD_URL = "/comment-add";
    public static final String COMMENT_DELETE_URL = "/comment-delete";

    private final BookService bookService;

    @GetMapping(BOOK_LIST_URL)
    public String bookList(Model model) {
        model.addAttribute("books", bookService.getBookList());
        return "list";
    }

    @GetMapping(value = BOOK_EDIT_URL)
    public String bookAdd(Model model) {
        Book book = Book.of("", Author.of(""), Genre.of(""));
        model.addAttribute("book", book);
        return "edit";
    }

    @GetMapping(value = BOOK_EDIT_URL, params = {"id"})
    public String bookEdit(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        return "edit";
    }

    @PostMapping(value = BOOK_EDIT_URL, params = {"method=save"})
    public RedirectView bookSave(@RequestParam("id") long id, @RequestParam("name") String bookName, @RequestParam("author") String authorName, @RequestParam(value = "genre") String genreName) {
        bookService.edit(id, bookName, authorName, genreName);
        return new RedirectView(BOOK_LIST_URL);
    }

    @PostMapping(value = BOOK_EDIT_URL, params = {"method=delete"})
    public RedirectView bookDelete(@RequestParam("id") long id) {
        bookService.remove(id);
        return new RedirectView(BOOK_LIST_URL);
    }

    @GetMapping(value = COMMENT_LIST_URL)
    public String commentList(@RequestParam("bookId") long bookId, Model model) {
        model.addAttribute("bookId", bookId);
        model.addAttribute("comments", bookService.getCommentList(bookId));
        return "comment-list";
    }

    @PostMapping(COMMENT_ADD_URL)
    public RedirectView commentAdd(@RequestParam("bookId") long bookId, @RequestParam("text") String commentText, RedirectAttributes redirectAttributes) {
        bookService.addComment(bookId, commentText);
        redirectAttributes.addAttribute("bookId", bookId);
        return new RedirectView(COMMENT_LIST_URL);
    }

    @PostMapping(COMMENT_DELETE_URL)
    public RedirectView commentDelete(@RequestParam("bookId") long bookId, @RequestParam("commentId") long commentId, RedirectAttributes redirectAttributes) {
        bookService.removeComment(commentId);
        redirectAttributes.addAttribute("bookId", bookId);
        return new RedirectView(COMMENT_LIST_URL);
    }
}
