package com.cxctis.fileconverter.conversion.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DocumentConverter {

    private static final Logger log = LoggerFactory.getLogger(DocumentConverter.class);

    public void convert(File inputFile, File outputFile) throws Exception {
        log.info("Starting document conversion from {} to {}", inputFile.getName(), outputFile.getName());

        ProcessBuilder processBuilder = new ProcessBuilder(
                "pandoc",
                inputFile.getAbsolutePath(),
                "-o",
                outputFile.getAbsolutePath(),
                "-V",
                "geometry:margin=1in",
                "--pdf-engine=xelatex"
        );

        processBuilder.redirectErrorStream(true);

        Process process = null;
        try {
            process = processBuilder.start();

            String output;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                output = reader.lines().collect(Collectors.joining("\n"));
            }

            boolean finished = process.waitFor(1, TimeUnit.MINUTES);
            if (!finished) {
                process.destroy();
                throw new RuntimeException("Pandoc conversion timed out.");
            }

            int exitCode = process.exitValue();
            if (exitCode == 0) {
                // If there was any non-error output, log it at debug level
                if (!output.isEmpty()) {
                    log.debug("Pandoc output:\n{}", output);
                }
                log.info("Document conversion successful: {}", outputFile.getAbsolutePath());
            } else {
                // Include Pandoc's output in the error! ---
                String errorMessage = String.format(
                        "Pandoc conversion failed with exit code: %d.\nPandoc Output:\n%s",
                        exitCode,
                        output
                );
                throw new RuntimeException(errorMessage);
            }

        } catch (Exception e) {
            log.error("Error running Pandoc command.", e);
            throw e;
        }
    }
}