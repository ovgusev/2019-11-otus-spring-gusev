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
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class BookController {
    static final String BOOK_LIST_URL = "/";
    static final String BOOK_EDIT_URL = "/edit";

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
    public RedirectView bookSave(Book book) {
        bookService.edit(book.getId(), book.getName(), book.getAuthor().getName(), book.getGenre().getName());
        return new RedirectView(BOOK_LIST_URL);
    }

    @PostMapping(value = BOOK_EDIT_URL, params = {"method=delete"})
    public RedirectView bookDelete(@RequestParam("id") long id) {
        bookService.remove(id);
        return new RedirectView(BOOK_LIST_URL);
    }
}
