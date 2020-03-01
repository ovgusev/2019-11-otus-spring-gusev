package com.ovgusev.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "comments")
@NamedEntityGraph(
        name = "Comment.book",
        attributeNodes = {
                @NamedAttributeNode(value = "book", subgraph = "Book.authorGenre")
        },
        subgraphs = {
                @NamedSubgraph(name = "Book.authorGenre", attributeNodes = {
                        @NamedAttributeNode("author"),
                        @NamedAttributeNode("genre")
                })
        }
)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Book.class)
    @JoinColumn(name = "book_id", nullable = false, updatable = false)
    private Book book;

    @Column(name = "text", nullable = false, updatable = false)
    private String text;

    @Column(name = "insert_date", nullable = false, updatable = false)
    private LocalDateTime insertDate;

    public static Comment of(Book book, String text) {
        return new Comment()
                .setBook(book)
                .setText(text)
                .setInsertDate(LocalDateTime.now());
    }
}
