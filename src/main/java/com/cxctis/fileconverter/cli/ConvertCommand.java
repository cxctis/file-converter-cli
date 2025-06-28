package com.cxctis.fileconverter.cli;

import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "convert", description = "Converts a file from one format to another.")
public class ConvertCommand implements Callable<Integer> {

    /**
     * Captures positional arguments from the command line
     */
    @CommandLine.Parameters(index = "0", description = "The file to be converted.")
    private File inputFile;

    /**
     * Option: captures name arguments, like "-o" or "--output".
     * This is optional; if the user doesn't provide it, the 'outputFile' variable will be null.
     */
    @CommandLine.Option(names = {"-o", "--output"},
            description = "The output file path. if not specified, a default name will be used.")
    private File outputFile;

    /**
     * Main logic to get executed when 'convert' cmd is called
     * @return an integer exit code. 0 typically means success.
     * @throws Exception if an error occurs
     */
    @Override
    public Integer call() throws Exception {
        if (!inputFile.exists()) {
            System.err.println("Error: Input file does not exist: " + inputFile.getAbsolutePath());
            return 1; // Return a non-zero exit code to indicate an error
        }

        System.out.println("--- Conversion Task ---");
        System.out.println("Input file provided: " + inputFile.getAbsolutePath());

        if (outputFile != null) {
            System.out.println("Output file provided: " + outputFile.getAbsolutePath());
        } else {
            System.out.println("Output file not specified. A default name will be generated later.");
        }

        //TODO: Add the real conversion logic

        return 0; // Success
    }
}
