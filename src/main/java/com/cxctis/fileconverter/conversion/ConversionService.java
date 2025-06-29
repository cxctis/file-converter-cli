package com.cxctis.fileconverter.conversion;

import com.cxctis.fileconverter.conversion.converters.DocumentConverter;
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

    public void convertFile(File inputFile, File outputFile) throws Exception {
        String mimeType = fileTypeDetector.detectFileType(inputFile);
        log.info("MIME type {} detected. Deciding which converter to use.", mimeType);

        File finalOutputFile = outputFile;

        if (mimeType.startsWith("text/") || mimeType.equals("application/json") || mimeType.equals("application/xml")) {
            log.info("Routing to DocumentConverter.");
            if (finalOutputFile == null) {
                // If no output is given for a doc, use a default PDF
                finalOutputFile = FileUtil.replaceExtension(inputFile, "pdf");
                log.info("No output file specified. Defaulting to: {}", finalOutputFile.getName());
            }
            documentConverter.convert(inputFile, finalOutputFile);
        }
         else if (mimeType.startsWith("video/")) {
             log.info("Routing to MultimediaConverter.");
             if (finalOutputFile == null) {
                 // If no output is given for a video, let's default to extracting audio as mp3.
                 finalOutputFile = FileUtil.replaceExtension(inputFile, "mp3");
                 log.info("No output file specified. Defaulting to {}", finalOutputFile.getName());
             }
             multimediaConverter.convert(inputFile, finalOutputFile);
         }
         else if (mimeType.startsWith("audio/")) {
             log.info("Routing to MultimediaConverter.");
             if (finalOutputFile == null) {
                 finalOutputFile = FileUtil.replaceExtension(inputFile, "mp3");
                 log.info("No output file specified. Defaulting to: {}", finalOutputFile.getName());
             }
             multimediaConverter.convert(inputFile, finalOutputFile);
        }
        else {
            throw new UnsupportedOperationException("File conversion for MIME type " + mimeType + " is not yet supported.");
        }
    }
}
