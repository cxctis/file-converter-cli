package com.cxctis.fileconverter.conversion;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

/**
 * Responsible for detecting the MIME type of file
 * using Apache Tika
 */
public class TikaFileTypeDetector {

    private final Tika tika;

    public TikaFileTypeDetector() {
        this.tika = new Tika();
    }

    /**
     * Detect the MIME type of the given file
     *
     * @param file The file whose type needs to be detected
     * @return A string representing the MIME type (e.g., "text/plain", "image/png").
     * @throws java.io.IOException if the file cannot be read.
     */
    public String detectFileType(File file) throws IOException {
        return tika.detect(file);
    }
}
