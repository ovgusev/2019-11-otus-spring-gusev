package com.ovgusev.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToOne(targetEntity = Author.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(targetEntity = Genre.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public static Book of(String name, Author author, Genre genre) {
        return new Book()
                .setName(name)
                .setAuthor(author)
                .setGenre(genre);
    }
}
