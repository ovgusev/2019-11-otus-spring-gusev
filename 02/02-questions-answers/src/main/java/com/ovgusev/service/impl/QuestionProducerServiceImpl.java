package com.ovgusev.service.impl;

import com.ovgusev.domain.Question;
import com.ovgusev.service.QuestionProducerService;
import com.ovgusev.service.ResourceService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class QuestionProducerServiceImpl implements QuestionProducerService {
    private final ResourceService resourceService;

    public QuestionProducerServiceImpl(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        CSVParser csvParser;

        try {
            csvParser = new CSVParser(new InputStreamReader(resourceService.getQuestionsAndAnswers()), CSVFormat.DEFAULT);

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
