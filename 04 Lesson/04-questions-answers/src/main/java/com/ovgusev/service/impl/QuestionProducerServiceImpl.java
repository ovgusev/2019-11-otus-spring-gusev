package com.ovgusev.service.impl;

import com.ovgusev.config.LocaleConfig;
import com.ovgusev.domain.Question;
import com.ovgusev.exceptions.IOAppException;
import com.ovgusev.service.QuestionProducerService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class QuestionProducerServiceImpl implements QuestionProducerService {
    private final ResourceLoader resourceLoader;
    private final LocaleConfig localeConfig;

    public QuestionProducerServiceImpl(ResourceLoader resourceLoader, LocaleConfig localeConfig) {
        this.resourceLoader = resourceLoader;
        this.localeConfig = localeConfig;
    }

    @Override
    public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        InputStream questionsInputStream;
        CSVParser csvParser;
        String filePath = getCSVFileName();

        try {
            questionsInputStream = resourceLoader.getResource(filePath).getInputStream();
            csvParser = new CSVParser(new InputStreamReader(questionsInputStream), CSVFormat.DEFAULT);

            csvParser.forEach(strings -> {
                Iterator<String> iterator = strings.iterator();
                questions.add(new Question(iterator.next(), new ArrayList<>() {{
                            iterator.forEachRemaining(this::add);
                        }})
                );
            });
        } catch (IOException e) {
            throw new IOAppException("Error while reading file", e);
        }

        return questions;
    }

    private String getCSVFileName() {
        return localeConfig.getFileBasename() + "_" + LocaleContextHolder.getLocale().getLanguage() + ".csv";
    }
}
