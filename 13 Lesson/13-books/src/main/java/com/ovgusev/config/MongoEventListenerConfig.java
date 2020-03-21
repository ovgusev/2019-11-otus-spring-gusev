package com.ovgusev.config;

import com.ovgusev.domain.Book;
import com.ovgusev.repository.CommentRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

@Component
public class MongoEventListenerConfig extends AbstractMongoEventListener<Book> {
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        Document source = event.getSource();

        ObjectId objectId = (ObjectId) source.get("_id");

        commentRepository.findByBookId(objectId.toString())
                .forEach(comment -> commentRepository.delete(comment));

        super.onBeforeDelete(event);
    }
}
