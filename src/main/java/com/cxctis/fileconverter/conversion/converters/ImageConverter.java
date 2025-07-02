package com.cxctis.fileconverter.conversion.converters;

import com.cxctis.fileconverter.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageConverter {

    private static final Logger log = LoggerFactory.getLogger(ImageConverter.class);

    public void convert(File inputFile, File outputFile) throws Exception {
        log.info("Starting image conversion from {} to {}", inputFile.getName(), outputFile.getName());

        BufferedImage image = ImageIO.read(inputFile);

        if (image == null) {
            throw new IOException("Could not read the input file as an image." +
                    "It may be corrupted or in an unsupported format.");
        }

        String formatName = FileUtil.getFileExtension(outputFile);
        if (formatName.isEmpty()) {
            throw new IllegalArgumentException("Output file must have an extension " +
                    "to determine the format (e.g., .png, .jpg).");
        }

        boolean success = ImageIO.write(image, formatName, outputFile);

        if (success) {
            log.info("Image conversion successful: {}", outputFile.getAbsolutePath());
        } else {
            throw new IOException("Could not find a converter for the specified format: " + formatName);
        }
    }
}
