package com.ovgusev.config;

import com.ovgusev.domain.Book;
import com.ovgusev.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoEventListenerConfig extends AbstractMongoEventListener<Book> {
    private final CommentRepository commentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        Document source = event.getSource();

        ObjectId objectId = (ObjectId) source.get("_id");

        commentRepository.findByBookId(objectId.toString())
                .forEach(commentRepository::delete);

        super.onBeforeDelete(event);
    }
}
