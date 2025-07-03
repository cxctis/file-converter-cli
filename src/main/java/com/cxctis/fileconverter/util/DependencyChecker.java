package com.cxctis.fileconverter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DependencyChecker {

    private static final Logger log = LoggerFactory.getLogger(DependencyChecker.class);

    /**
     * Checks if a command is available on the system's PATH.
     * @param command The command to check (e.g., "pandoc", "ffmpeg").
     * @return true if the command can be executed, false otherwise.
     */
    public boolean isCommandAvailable(String command, String dashChar) {
        log.debug("Checking for dependency: {}", command);
        try {
            // We run the command with a "version" flag, which is standard for most CLI tools.
            // It should return an exit code of 0 if it exists.
            ProcessBuilder processBuilder = new ProcessBuilder(command, dashChar + "version");
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.debug("Found dependency: {}", command);
                return true;
            } else {
                log.warn("Dependency {} found, but '--version' return non-zero exit code: {}", command, exitCode);
                return false; // Command exists but isn't behaving as expected.
            }
        } catch (IOException e) {
            log.error("Dependency not found: {}. The command could not be executed.", command);
            return false;
        } catch (InterruptedException e) {
            log.error("Thread was interrupted while waiting for command: {}", command);
            return false;
        }
    }
}
