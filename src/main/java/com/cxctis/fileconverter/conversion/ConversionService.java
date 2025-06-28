package com.cxctis.fileconverter.conversion;

import com.cxctis.fileconverter.conversion.converters.DocumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ConversionService {

    private static final Logger log = LoggerFactory.getLogger(ConversionService.class);

    private final TikaFileTypeDetector fileTypeDetector = new TikaFileTypeDetector();
    private final DocumentConverter documentConverter = new DocumentConverter();
    // In the future, we will add more converters here, e.g., MultimediaConverter

    public void convertFile(File inputFile, File outputFile) throws Exception {
        String mimeType = fileTypeDetector.detectFileType(inputFile);
        log.info("MIME type {} detected. Deciding which converter to use.", mimeType);

        // This is our main routing logic.
        if (mimeType.startsWith("text/") || mimeType.equals("application/json") || mimeType.equals("application/xml")) {
            documentConverter.convert(inputFile, outputFile);
        }
        // else if (mimeType.startsWith("video/") || mimeType.startsWith("audio/")) {
        //     multimediaConverter.convert(inputFile, outputFile);
        // }
        else {
            throw new UnsupportedOperationException("File conversion for MIME type " + mimeType + " is not yet supported.");
        }
    }
}
