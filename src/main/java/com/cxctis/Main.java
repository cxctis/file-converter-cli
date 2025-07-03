package com.cxctis;

import com.cxctis.fileconverter.cli.ConvertCommand;
import com.cxctis.fileconverter.util.DependencyChecker;
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

        DependencyChecker checker = new DependencyChecker();

        System.out.println("Checking required dependencies...");

        if (!checker.isCommandAvailable("pandoc", "--")) {
            System.err.println("---------------------------------------------------------------");
            System.err.println("ERROR: Pandoc is not installed or not found in your system's PATH.");
            System.err.println("Please install it from https://pandoc.org/installing.html");
            System.err.println("---------------------------------------------------------------");
            System.exit(1); // Exit with an error code
        }

        if (!checker.isCommandAvailable("ffmpeg", "-")) {
            System.err.println("---------------------------------------------------------------");
            System.err.println("ERROR: FFmpeg is not installed or not found in your system's PATH.");
            System.err.println("Please install it from https://ffmpeg.org/download.html");
            System.err.println("---------------------------------------------------------------");
            System.exit(1); // Exit with an error code
        }
        System.out.println("All dependencies are satisfied.");

        // Picocli will automatically parse the arguments and execute the correct subcommand.
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}