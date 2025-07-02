package com.cxctis.fileconverter.conversion;

import com.cxctis.fileconverter.conversion.converters.DocumentConverter;
import com.cxctis.fileconverter.conversion.converters.ImageConverter;
import com.cxctis.fileconverter.conversion.converters.MultimediaConverter;
import com.cxctis.fileconverter.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ConversionService {

    private static final Logger log = LoggerFactory.getLogger(ConversionService.class);

    private final TikaFileTypeDetector fileTypeDetector = new TikaFileTypeDetector();
    private final DocumentConverter documentConverter = new DocumentConverter();
    private final MultimediaConverter multimediaConverter = new MultimediaConverter();
    private final ImageConverter imageConverter = new ImageConverter();

    public void convertFile(File inputFile, File outputFile) throws Exception {
        String mimeType = fileTypeDetector.detectFileType(inputFile);
        log.info("MIME type {} detected. Deciding which converter to use.", mimeType);

        if (mimeType.startsWith("text/") || mimeType.equals("application/json") || mimeType.equals("application/xml")) {
            log.info("Routing to DocumentConverter.");
            documentConverter.convert(inputFile, outputFile);
        } else if (mimeType.startsWith("video/") || mimeType.startsWith("audio/")) {
             multimediaConverter.convert(inputFile, outputFile);
        } else if (mimeType.startsWith("image/")) {
            log.info("Routing to ImageConverter.");
            imageConverter.convert(inputFile, outputFile);
        } else {
            throw new UnsupportedOperationException("File conversion for MIME type " + mimeType + " is not yet supported.");
        }
    }
}
