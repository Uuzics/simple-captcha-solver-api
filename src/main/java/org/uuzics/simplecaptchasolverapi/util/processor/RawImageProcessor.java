package org.uuzics.simplecaptchasolverapi.util.processor;

import java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RawImageProcessor {
    public static BufferedImage parseBase64ToBufferedImage(String base64ImageString) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(base64ImageString.getBytes());
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }
}
