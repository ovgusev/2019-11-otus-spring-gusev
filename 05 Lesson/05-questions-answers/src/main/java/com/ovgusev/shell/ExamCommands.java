package com.ovgusev.shell;

import com.ovgusev.constants.MessagesConsts;
import com.ovgusev.domain.Answer;
import com.ovgusev.service.AskingService;
import com.ovgusev.service.I18nMessageService;
import com.ovgusev.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class ExamCommands {
    private final AskingService askingService;
    private final ResultService resultService;
    private final I18nMessageService messageService;

    private String userName;
    private List<Answer> lastResult;

    @ShellMethod(value = "Start exam", key = {"exam", "start", "start-exam"})
    public String startExam() {
        if (userName == null) {
            return askLogin();
        } else {
            setLastResult(askingService.askQuestions());
            return printResults();
        }
    }

    @ShellMethod(value = "Login", key = {"login", "user"})
    public String login(@ShellOption(value = "name", defaultValue = "") String userName) {
        if (userName.isEmpty()) {
            return askLogin();
        } else {
            setUserName(userName);
            return messageService.getMessage(MessagesConsts.GREETINGS_PROPERTY, userName);
        }
    }

    @ShellMethod(value = "Print last results", key = {"hist", "result"})
    public String printLastResults() {
        if (lastResult != null) {
            return printResults();
        } else {
            return null;
        }
    }

    private String askLogin() {
        return messageService.getMessage(MessagesConsts.ASK_LOGIN_PROPERTY);
    }

    private String printResults() {
        return resultService.printResults(userName, lastResult);
    }

    private synchronized void setUserName(String userName) {
        this.userName = userName;
    }

    private synchronized void setLastResult(List<Answer> lastResult) {
        this.lastResult = lastResult;
    }
}
