package com.ovgusev.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
//@Table(name = "comments")
public class Comment {
    @Id
    private long id;

    private Book book;

    private String text;

    private LocalDateTime insertDate;

    public static Comment of(Book book, String text) {
        return new Comment()
                .setBook(book)
                .setText(text)
                .setInsertDate(LocalDateTime.now());
    }
}
