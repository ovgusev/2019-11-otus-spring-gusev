package com.ovgusev.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Book {
    private long id;

    private String name;

    private Author author;

    private Genre genre;

    public static Book of(String name, Author author, Genre genre) {
        return new Book()
                .setName(name)
                .setAuthor(author)
                .setGenre(genre);
    }
}
