package com.cxctis.fileconverter.cli;

import com.cxctis.fileconverter.conversion.ConversionService;
import com.cxctis.fileconverter.util.FileUtil;
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

    @Option(names = {"-o", "--output"}, description = "The output file path. If not provided, a name with be generated automatically.")
    private File outputFile;

    @Option(names = {"-f", "--format"}, description = "The target output format (e.g.,e.g., pdf, docx, mp3). Use this OR --output.")
    private String targetFormat;

    @Option(names = {"-n", "--name"}, description = "Set a new name for the output file (requires -f).")
    private String newOutputFileName;

    private final ConversionService conversionService = new ConversionService();

    @Override
    public Integer call() {
        if (outputFile == null && targetFormat == null) {
            log.error("Validation failed: You must specify either a full output path with --output OR a target format with --format.");
            return 1;
        }

        if (newOutputFileName != null && targetFormat == null) {
            log.error("Validation failed: The --name option can only be used when specifying a --format.");
            return 1;
        }

        // If -o is not provided, we must generate it using -f and optionally -n.
        if (outputFile == null) {
            outputFile = FileUtil.createOutputFile(inputFile, newOutputFileName, targetFormat);
        }

        try {
            if (!inputFile.exists()) {
                log.error("Input file does not exist: {}", inputFile.getAbsolutePath());
                return 1;
            }
            log.info("--- Conversion Task Started ---");

            conversionService.convertFile(inputFile, outputFile);

            log.info("--- Conversion Task Finished Successfully ---");
            return 0; // SUCCESS
        } catch (Exception e) {
            log.error("A critical error occurred during conversion. Reason: {}", e.getMessage(), e);
            return 1; // FAILURE
        }
    }
}