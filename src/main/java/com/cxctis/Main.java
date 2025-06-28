package com.cxctis;

import com.cxctis.fileconverter.cli.ConvertCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "converter",
        mixinStandardHelpOptions = true,
        version = "File Converter CLI 1.0",
        description = "A secure, all-in-one tool to convert almost any file type.",
        subcommands = {
                ConvertCommand.class // Register our new command here
        })
public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}