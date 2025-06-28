package com.cxctis.fileconverter.cli;

import com.cxctis.fileconverter.conversion.TikaFileTypeDetector;
import org.slf4j.Logger; // Import from org.slf4j
import org.slf4j.LoggerFactory; // Import from org.slf4j
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

import static org.slf4j.LoggerFactory.getLogger;

@CommandLine.Command(name = "convert", description = "Converts a file from one format to another.")
public class ConvertCommand implements Callable<Integer> {

    // Logger
    private static final Logger log = getLogger(ConvertCommand.class);

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
            //System.err.println("[ERROR]: Input file does not exist: " + inputFile.getAbsolutePath());
            log.error("Input file does not exist: {}", inputFile.getAbsolutePath());
            return 1; // Return a non-zero exit code to indicate an error
        }

        log.info("--- Conversion Task Started ---");
        log.info("Input file: {}", inputFile.getAbsolutePath());

        // --- File Detection Logic ---
        TikaFileTypeDetector detector = new TikaFileTypeDetector();
        String mimeType = detector.detectFileType(inputFile);
        log.info("Detected MIME type: {}", mimeType);

        if (outputFile != null) {
            log.info("Output file provided: {}", outputFile.getAbsolutePath());
        } else {
            log.info("Output file not specified. A default name will be generated later.");
        }

        //TODO: Add the real conversion logic

        return 0; // Success
    }
}
