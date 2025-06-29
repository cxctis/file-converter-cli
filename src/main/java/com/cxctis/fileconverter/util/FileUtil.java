package com.cxctis.fileconverter.util;

import java.io.File;

public class FileUtil {

    /**
     * Replaces the extension of a filename with a new one.
     * @param originalFile The original file.
     * @param newExtension The new extension (without the dot).
     * @return A new File object with the new extension.
     */
    public static File replaceExtension(File originalFile, String newExtension) {
        String originalName = originalFile.getName();
        int lastDot = originalName.lastIndexOf('.');
        String nameWithoutExtension = (lastDot == -1)
                ? originalName : originalName.substring(0, lastDot);
        String newFileName = nameWithoutExtension + "." + newExtension;

        // Create the new file in the same directory as the original
        return new File(originalFile.getParent(), newFileName);
    }
}
