package com.ovgusev.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.ovgusev.domain.Book;
import com.ovgusev.domain.Comment;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {
    @ChangeSet(order = "000", id = "dropDB", author = "ovgusev", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initBooks", author = "ovgusev", runAlways = true)
    public void initPersons(MongoTemplate template) {
        Book book = template.save(Book.of("The Hobbit", "J.R.R. Tolkien", "Action and Adventure"));
        template.save(Comment.of(book, "comment text 1"));
        template.save(Comment.of(book, "comment text 2"));

        template.save(Book.of("The Three Musketeers", "Alexandre Dumas", "Action and Adventure"));
        template.save(Book.of("Life of Pi", "Yann Martel", "Action and Adventure"));
        template.save(Book.of("To Kill a Mockingbird", "Harper Lee", "Classic"));
        template.save(Book.of("1984", "George Orwell", "Classic"));
        template.save(Book.of("Romeo and Juliet", "William Shakespeare", "Classic"));
        template.save(Book.of("Hamlet", "William Shakespeare", "Drama"));
    }
}
