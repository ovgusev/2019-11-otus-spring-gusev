package com.ovgusev.controller;

import com.ovgusev.domain.Author;
import com.ovgusev.domain.Book;
import com.ovgusev.domain.Genre;
import com.ovgusev.service.BookService;
import com.ovgusev.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import static com.ovgusev.controller.BookController.BOOK_EDIT_URL;
import static com.ovgusev.controller.BookController.BOOK_LIST_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DisplayName("Testing methods of class BookController")
class BookControllerTest {
    static final Book TEST_BOOK = new Book()
            .setId(1L)
            .setName("book_name")
            .setAuthor(Author.of("author_name"))
            .setGenre(Genre.of("genre_name"));

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @Test
    void bookList() throws Exception {
        ModelAndView modelAndView = mvc.perform(get(BOOK_LIST_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getModelAndView();

        assertEquals("list", modelAndView.getViewName());
        assertEquals(1, modelAndView.getModel().size());
        assertTrue(((List) modelAndView.getModel().get("books")).isEmpty());

        verify(bookService, times(1)).getBookList();
    }

    @Test
    void bookAdd() throws Exception {
        ModelAndView modelAndView = mvc.perform(get(BOOK_EDIT_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getModelAndView();

        assertEquals("edit", modelAndView.getViewName());
        assertEquals(2, modelAndView.getModel().size());
        assertEquals(0L, ((Book) modelAndView.getModel().get("book")).getId());

        verify(bookService, never()).findById(0L);
    }

    @Test
    void bookEdit() throws Exception {
        when(bookService.findById(TEST_BOOK.getId())).thenReturn(TEST_BOOK);

        ModelAndView modelAndView = mvc.perform(get(BOOK_EDIT_URL)
                .param("id", Long.toString(TEST_BOOK.getId())))
                .andExpect(status().isOk())
                .andReturn()
                .getModelAndView();

        assertEquals("edit", modelAndView.getViewName());
        assertEquals(2, modelAndView.getModel().size());
        assertEquals(TEST_BOOK, modelAndView.getModel().get("book"));

        verify(bookService, times(1)).findById(TEST_BOOK.getId());
    }

    @Test
    void bookSave() throws Exception {
        ModelAndView modelAndView = mvc.perform(post(BOOK_EDIT_URL)
                .param("method", "save")
                .param("id", Long.toString(TEST_BOOK.getId()))
                .param("name", TEST_BOOK.getName())
                .param("author", TEST_BOOK.getAuthor().getName())
                .param("genre", TEST_BOOK.getGenre().getName()))
                .andExpect(status().isFound())
                .andReturn()
                .getModelAndView();

        assertEquals(BOOK_LIST_URL, ((RedirectView) modelAndView.getView()).getUrl());
        assertEquals(0, modelAndView.getModel().size());

        verify(bookService, times(1)).edit(TEST_BOOK.getId(), TEST_BOOK.getName(), TEST_BOOK.getAuthor().getName(), TEST_BOOK.getGenre().getName());
    }

    @Test
    void bookDelete() throws Exception {
        ModelAndView modelAndView = mvc.perform(post(BOOK_EDIT_URL)
                .param("method", "delete")
                .param("id", Long.toString(TEST_BOOK.getId())))
                .andExpect(status().isFound())
                .andReturn()
                .getModelAndView();

        assertEquals(BOOK_LIST_URL, ((RedirectView) modelAndView.getView()).getUrl());
        assertEquals(0, modelAndView.getModel().size());

        verify(bookService, times(1)).remove(TEST_BOOK.getId());
    }
}