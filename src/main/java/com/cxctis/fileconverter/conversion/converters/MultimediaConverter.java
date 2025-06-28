package com.cxctis.fileconverter.conversion.converters;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MultimediaConverter {

    private static final Logger log = LoggerFactory.getLogger(MultimediaConverter.class);
    private static final List<String> AUDIO_ONLY_FORMATS = Arrays.asList("mp3", "wav", "ogg", "flac", "aac");

    public void convert(File inputFile, File outputFile) throws Exception {
        log.info("Starting multimedia conversion from {} to {}", inputFile.getName(), outputFile.getName());

        //FFmpeg ffmpeg = new FFmpeg("ffmpeg");
        FFprobe ffprobe = new FFprobe("ffprobe");
        FFmpegProbeResult probeResult = ffprobe.probe(inputFile.getAbsolutePath());

        // Pre-flight check for audio stream
        String outputExtension = getFileExtension(outputFile);
        if (AUDIO_ONLY_FORMATS.contains(outputExtension)) {
            boolean hasAudioStream = probeResult.getStreams().stream()
                    .anyMatch(stream -> stream.codec_type == FFmpegStream.CodecType.AUDIO);

            if (!hasAudioStream) {
                // Throw a user-friendly exception instead of letting FFmpeg fail cryptically
                throw new IllegalArgumentException("Conversion to audio-only format ." + outputExtension +
                        " failed because the source file has no audio track.");
            }
        }

        // FFmpegBuilder provides a type-safe way to construct complex cmds
        FFmpeg ffmpeg = new FFmpeg("ffmpeg");
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(inputFile.getAbsolutePath())
                .overrideOutputFiles(true) // Overwrite output file if exists
                .addOutput(outputFile.getAbsolutePath())
                .done();

        // Runs the cmd
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        try {
            // Run the conversion
            executor.createJob(builder).run();
            log.info("Multimedia conversion successful: {}", outputFile.getAbsolutePath());
        } catch (Exception e) {
            log.error("Error running FFmpeg command", e);
            throw new RuntimeException("FFmpeg conversion failed.", e);
        }
    }

    // Helper method to get a file's extension
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1).toLowerCase();
    }
}
