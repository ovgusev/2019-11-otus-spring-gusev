package com.ovgusev.service.impl;

import com.ovgusev.service.CommandLineIOService;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.util.Scanner;

@Service
public class CommandLineIOServiceImpl implements CommandLineIOService {
    private final Scanner scanner = new Scanner(System.in);
    private final PrintStream out = System.out;

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public void printLine(String line) {
        out.println(line);
    }
}
