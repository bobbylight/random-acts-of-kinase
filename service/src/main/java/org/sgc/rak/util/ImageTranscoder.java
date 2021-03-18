package org.sgc.rak.util;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Converts images from one format to another.
 */
public class ImageTranscoder {

    /**
     * Generates a PNG file's contents from SVG data.
     *
     * @param fileName The name of the SVG file or resource being converted.  Only used for error messages.
     * @param in The SVG data, as an input stream.  This will be closed when this method returns.
     * @param width The width of the PNG file to generate.
     * @param height The height of the PNG file to generate.
     * @return The PNG file data, as a byte array.
     * @throws IOException If an IO error occurs.
     */
    public byte[] svgToPng(String fileName, InputStream in, Float width, Float height) throws IOException {

        PNGTranscoder t = new PNGTranscoder();
        if (width != null) {
            t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width);
        }
        if (height != null) {
            t.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, height);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(baos);

        try (BufferedInputStream bin = new BufferedInputStream(in)) {
            TranscoderInput input = new TranscoderInput(bin);
            try {
                t.transcode(input, output);
            } catch (TranscoderException e) {
                throw new IOException("Error transcoding SVG to PNG for " + fileName, e);
            }
        }

        return baos.toByteArray();
    }
}
