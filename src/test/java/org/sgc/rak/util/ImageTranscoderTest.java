package org.sgc.rak.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ImageTranscoderTest {

    private ImageTranscoder transcoder;

    @Before
    public void setUp() {
        transcoder = new ImageTranscoder();
    }

    @Test
    public void testSvgToPng_happyPath() throws IOException {

        String svg = "<svg version=\"1.1\" id=\"topsvg\"\n" +
            "xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n" +
            "xmlns:cml=\"http://www.xml-cml.org/schema\" x=\"0\" y=\"0\" width=\"200px\" height=\"200px\" viewBox=\"0 0 100 100\">\n" +
            "</svg>";

        ByteArrayInputStream in = new ByteArrayInputStream(svg.getBytes(StandardCharsets.UTF_8));
        Assert.assertTrue(transcoder.svgToPng("foo.svg", in, 400f, 400f).length > 0);
    }

    @Test(expected = IOException.class)
    public void testSvgToPng_error_invalidSvg() throws IOException {

        String notSvg = "This is not SVG data";

        ByteArrayInputStream in = new ByteArrayInputStream(notSvg.getBytes(StandardCharsets.UTF_8));
        transcoder.svgToPng("foo.svg", in, 400f, 400f);
    }
}
