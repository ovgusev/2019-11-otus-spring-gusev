package com.ovgusev.controller;

import com.ovgusev.service.BookService;
import com.ovgusev.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

import static com.ovgusev.controller.BookControllerTest.TEST_BOOK;
import static com.ovgusev.controller.CommentController.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WithMockUser("mockUser")
@DisplayName("Testing methods of class CommentController")
class CommentControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @Test
    void commentList() throws Exception {
        ModelAndView modelAndView = mvc.perform(get(COMMENT_LIST_URL)
                .param("bookId", Long.toString(TEST_BOOK.getId())))
                .andExpect(status().isOk())
                .andReturn()
                .getModelAndView();

        assertEquals("comment-list", modelAndView.getViewName());
        assertEquals(2, modelAndView.getModel().size());
        assertEquals(Long.toString(TEST_BOOK.getId()), modelAndView.getModel().get("bookId"));
        assertTrue(((List) modelAndView.getModel().get("comments")).isEmpty());

        verify(commentService, times(1)).getCommentList(TEST_BOOK.getId());
    }

    @Test
    void commentAdd() throws Exception {
        String COMMENT_TEXT = "comment_text";

        ModelAndView modelAndView = mvc.perform(post(COMMENT_ADD_URL)
                .param("bookId", Long.toString(TEST_BOOK.getId()))
                .param("text", COMMENT_TEXT))
                .andExpect(status().isFound())
                .andReturn()
                .getModelAndView();

        assertEquals(COMMENT_LIST_URL, ((RedirectView) modelAndView.getView()).getUrl());
        assertEquals(1, modelAndView.getModel().size());
        assertEquals(Long.toString(TEST_BOOK.getId()), modelAndView.getModel().get("bookId"));

        verify(commentService, times(1)).addComment(TEST_BOOK.getId(), COMMENT_TEXT);
    }

    @Test
    void commentDelete() throws Exception {
        long COMMENT_ID = 1L;

        ModelAndView modelAndView = mvc.perform(post(COMMENT_DELETE_URL)
                .param("bookId", Long.toString(TEST_BOOK.getId()))
                .param("commentId", Long.toString(COMMENT_ID)))
                .andExpect(status().isFound())
                .andReturn()
                .getModelAndView();

        assertEquals(COMMENT_LIST_URL, ((RedirectView) modelAndView.getView()).getUrl());
        assertEquals(1, modelAndView.getModel().size());
        assertEquals(Long.toString(TEST_BOOK.getId()), modelAndView.getModel().get("bookId"));

        verify(commentService, times(1)).removeComment(COMMENT_ID);
    }
}