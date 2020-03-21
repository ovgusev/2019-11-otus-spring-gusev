package com.ovgusev.service.impl;

import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;
import com.ovgusev.repository.BookRepository;
import com.ovgusev.repository.CommentRepository;
import com.ovgusev.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentList(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public void addComment(long bookId, String commentText) {
        Book book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);

        Comment comment = Comment.of(book, commentText);
        commentRepository.save(comment);
    }

    @Override
    public void removeComment(long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }
}
