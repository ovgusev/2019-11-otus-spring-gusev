package com.ovgusev.domain.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "comments")
public class NoSqlComment {
    @Id
    private String id;

    @DBRef
    private NoSqlBook book;

    private String text;

    private LocalDateTime insertDate;

    public static NoSqlComment of(NoSqlBook book, String text) {
        return new NoSqlComment()
                .setBook(book)
                .setText(text)
                .setInsertDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
