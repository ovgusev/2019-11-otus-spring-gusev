package com.ovgusev.service;

import com.ovgusev.domain.Question;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuestionProducerServiceImpl implements QuestionProducerService {
    private final String filePath;

    public QuestionProducerServiceImpl(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(filePath);

        CSVParser csvParser;

        try {
            csvParser = new CSVParser(new InputStreamReader(resourceAsStream), CSVFormat.DEFAULT);

            csvParser.forEach(strings -> {
                Iterator<String> iterator = strings.iterator();
                questions.add(new Question(iterator.next(), new ArrayList<>() {{
                            iterator.forEachRemaining(this::add);
                        }})
                );
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        return questions;
    }
}
