package com.ovgusev.controller;

import com.ovgusev.domain.Author;
import com.ovgusev.domain.Book;
import com.ovgusev.domain.Genre;
import com.ovgusev.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.ovgusev.controller.BookController.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@DisplayName("Testing methods of class BookController")
class BookControllerTest {
    private final Book TEST_BOOK = new Book()
            .setId(1L)
            .setName("book_name")
            .setAuthor(Author.of("author_name"))
            .setGenre(Genre.of("genre_name"));

    @Autowired
    private MockMvc mvc;

    @MockBean
    BookService bookService;

    @Test
    void bookList() throws Exception {
        mvc.perform(get(BOOK_LIST_URL))
                .andExpect(status().isOk());

        verify(bookService, times(1)).getBookList();
    }

    @Test
    void bookAdd() throws Exception {
        mvc.perform(get(BOOK_EDIT_URL))
                .andExpect(status().isOk());

        verify(bookService, never()).findById(0L);
    }

    @Test
    void bookEdit() throws Exception {
        when(bookService.findById(TEST_BOOK.getId())).thenReturn(TEST_BOOK);

        mvc.perform(get(BOOK_EDIT_URL)
                .param("id", Long.toString(TEST_BOOK.getId())))
                .andExpect(status().isOk());

        verify(bookService, times(1)).findById(TEST_BOOK.getId());
    }

    @Test
    void bookSave() throws Exception {
        mvc.perform(post(BOOK_EDIT_URL)
                .param("method", "save")
                .param("id", Long.toString(TEST_BOOK.getId()))
                .param("name", TEST_BOOK.getName())
                .param("author", TEST_BOOK.getAuthor().getName())
                .param("genre", TEST_BOOK.getGenre().getName())
        ).andExpect(status().isFound());

        verify(bookService, times(1)).edit(TEST_BOOK.getId(), TEST_BOOK.getName(), TEST_BOOK.getAuthor().getName(), TEST_BOOK.getGenre().getName());
    }

    @Test
    void bookDelete() throws Exception {
        mvc.perform(post(BOOK_EDIT_URL)
                .param("method", "delete")
                .param("id", Long.toString(TEST_BOOK.getId()))
        ).andExpect(status().isFound());

        verify(bookService, times(1)).remove(TEST_BOOK.getId());
    }

    @Test
    void commentList() throws Exception {
        mvc.perform(get(COMMENT_LIST_URL)
                .param("bookId", Long.toString(TEST_BOOK.getId()))
        ).andExpect(status().isOk());

        verify(bookService, times(1)).getCommentList(TEST_BOOK.getId());
    }

    @Test
    void commentAdd() throws Exception {
        String COMMENT_TEXT = "comment_text";

        mvc.perform(post(COMMENT_ADD_URL)
                .param("bookId", Long.toString(TEST_BOOK.getId()))
                .param("text", COMMENT_TEXT)
        ).andExpect(status().isFound());

        verify(bookService, times(1)).addComment(TEST_BOOK.getId(), COMMENT_TEXT);
    }

    @Test
    void commentDelete() throws Exception {
        long COMMENT_ID = 1L;

        mvc.perform(post(COMMENT_DELETE_URL)
                .param("bookId", Long.toString(TEST_BOOK.getId()))
                .param("commentId", Long.toString(COMMENT_ID))
        ).andExpect(status().isFound());

        verify(bookService, times(1)).removeComment(COMMENT_ID);
    }
}