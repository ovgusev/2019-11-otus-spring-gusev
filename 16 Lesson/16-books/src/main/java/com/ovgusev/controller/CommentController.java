package com.ovgusev.controller;

import com.ovgusev.service.CommentService;
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
public class CommentController {
    static final String COMMENT_LIST_URL = "/comment-list";
    static final String COMMENT_ADD_URL = "/comment-add";
    static final String COMMENT_DELETE_URL = "/comment-delete";

    private final CommentService commentService;

    @GetMapping(value = COMMENT_LIST_URL)
    public String commentList(@RequestParam("bookId") long bookId, Model model) {
        model.addAttribute("bookId", Long.toString(bookId));
        model.addAttribute("comments", commentService.getCommentList(bookId));
        return "comment-list";
    }

    @PostMapping(COMMENT_ADD_URL)
    public RedirectView commentAdd(@RequestParam("bookId") long bookId, @RequestParam("text") String commentText, RedirectAttributes redirectAttributes) {
        commentService.addComment(bookId, commentText);
        redirectAttributes.addAttribute("bookId", bookId);
        return new RedirectView(COMMENT_LIST_URL);
    }

    @PostMapping(COMMENT_DELETE_URL)
    public RedirectView commentDelete(@RequestParam("bookId") long bookId, @RequestParam("commentId") long commentId, RedirectAttributes redirectAttributes) {
        commentService.removeComment(commentId);
        redirectAttributes.addAttribute("bookId", bookId);
        return new RedirectView(COMMENT_LIST_URL);
    }
}
