package com.ovgusev.domain.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "books")
public class NoSqlBook {
    @Id
    private String id;

    private String name;

    private String author;

    private String genre;

    public static NoSqlBook of(String name, String author, String genre) {
        return new NoSqlBook()
                .setName(name)
                .setAuthor(author)
                .setGenre(genre);
    }
}
