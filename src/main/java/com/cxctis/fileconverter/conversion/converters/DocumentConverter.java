package com.cxctis.fileconverter.conversion.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class DocumentConverter {

    private static final Logger log = LoggerFactory.getLogger(DocumentConverter.class);

    public void convert(File inputFile, File outputFile) throws Exception {
        log.info("Starting document conversion from {} to {}", inputFile.getName(), outputFile.getName());

        // We use ProcessBuilder to securely create and run the external pandoc command.
        ProcessBuilder processBuilder = new ProcessBuilder(
                "pandoc",
                inputFile.getAbsolutePath(),
                "-o", // Output file option
                outputFile.getAbsolutePath(),
                "-V",
                "geometry:margin=1in"
        );

        // This ensures that error messages from the command are redirected to the main process stream
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            // Read the output from the command (useful for debugging)
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.debug("Pandoc output: {}", line);
                }
            }

            // Wait for the process to complete, but with a timeout to prevent it from hanging forever.
            boolean finished = process.waitFor(1, TimeUnit.MINUTES);
            if (!finished) {
                process.destroy();
                throw new RuntimeException("Pandoc conversion timed out.");
            }

            int exitCode = process.exitValue();
            if (exitCode == 0) {
                log.info("Document conversion successful: {}", outputFile.getAbsolutePath());
            } else {
                // Since we redirected the error stream, any errors would have been logged above.
                throw new RuntimeException("Pandoc conversion failed with exit code: " + exitCode);
            }

        } catch (Exception e) {
            log.error("Error running Pandoc command", e);
            throw e; // Re-throw the exception to be handled by the caller
        }
    }
}
