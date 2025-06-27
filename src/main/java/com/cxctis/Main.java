package com.cxctis;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "converter", mixinStandardHelpOptions = true, version = "File Converter CLI 1.0",
description = "A secure, all-in-one tool to convert any file type.")
public class Main implements Callable<Integer> {


    @Override
    public Integer call() throws Exception {
        System.out.println("File Converter CLI is running!");
        CommandLine.usage(this, System.out);
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}