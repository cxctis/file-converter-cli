package com.cxctis.fileconverter.cli;

import com.cxctis.fileconverter.conversion.ConversionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "convert",
        description = "Converts a file from one format to another.")
public class ConvertCommand implements Callable<Integer> {

    private static final Logger log = LoggerFactory.getLogger(ConvertCommand.class);

    @Parameters(index = "0", description = "The file to be converted.")
    private File inputFile;

    @Option(names = {"-o", "--output"}, description = "The output file path. If not provided, a name with be " +
            "generated automatically.")
    private File outputFile; // Made this required for simplicity for now

    private final ConversionService conversionService = new ConversionService();

    @Override
    public Integer call() {
        try {
            if (!inputFile.exists()) {
                log.error("Input file does not exist: {}", inputFile.getAbsolutePath());
                return 1;
            }
            log.info("--- Conversion Task Started ---");

            // Delegate all the complex logic to the conversion service
            conversionService.convertFile(inputFile, outputFile);

            log.info("--- Conversion Task Finished Successfully ---");
            return 0; // SUCCESS
        } catch (Exception e) {
            log.error("A critical error occurred during conversion: {}", e.getMessage());
            return 1; // FAILURE
        }
    }
}