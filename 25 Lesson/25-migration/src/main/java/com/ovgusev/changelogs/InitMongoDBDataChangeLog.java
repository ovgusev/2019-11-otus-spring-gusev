package com.ovgusev.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import com.ovgusev.domain.nosql.NoSqlBook;
import com.ovgusev.domain.nosql.NoSqlComment;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {
    @ChangeSet(order = "000", id = "dropDB", author = "ovgusev", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initBooks", author = "ovgusev", runAlways = true)
    public void initPersons(MongoTemplate template) {
        NoSqlBook book = template.save(NoSqlBook.of("The Hobbit", "J.R.R. Tolkien", "Action and Adventure"));
        template.save(NoSqlComment.of(book, "comment text 1"));
        template.save(NoSqlComment.of(book, "comment text 2"));

        template.save(NoSqlBook.of("The Three Musketeers", "Alexandre Dumas", "Action and Adventure"));
        template.save(NoSqlBook.of("Life of Pi", "Yann Martel", "Action and Adventure"));
        template.save(NoSqlBook.of("To Kill a Mockingbird", "Harper Lee", "Classic"));
        template.save(NoSqlBook.of("1984", "George Orwell", "Classic"));
        template.save(NoSqlBook.of("Romeo and Juliet", "William Shakespeare", "Classic"));
        template.save(NoSqlBook.of("Hamlet", "William Shakespeare", "Drama"));
    }
}
