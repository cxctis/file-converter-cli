package com.cxctis.fileconverter.util;

import java.io.File;

public class FileUtil {

    /**
     * Replaces the extension of a filename with a new one.
     * @param originalFile The original file.
     * @param newExtension The new extension (without the dot).
     * @return A new File object with the new extension.
     */
    public static File createOutputFile(File originalFile, String newBaseName, String newExtension) {
        String baseName;

        if (newBaseName == null || newBaseName.trim().isEmpty()) {
            // If no new name is provided, use the original file's name without its extension.
            String originalName = originalFile.getName();
            int lastDot = originalName.lastIndexOf('.');
            baseName = (lastDot == -1) ? originalName : originalName.substring(0, lastDot);
        } else {
            baseName = newBaseName;
        }

        String newFileName = baseName + "." + newExtension;

        // Create the new file in the same directory as the original
        return new File(originalFile.getParent(), newFileName);
    }
}
