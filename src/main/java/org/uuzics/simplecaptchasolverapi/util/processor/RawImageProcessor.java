/*
 * Copyright 2022 Uuzics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.uuzics.simplecaptchasolverapi.util.processor;

import java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Processor for parsing images in requests to BufferedImage
 */
public class RawImageProcessor {
    /**
     * Parse a Base64 encoded image string to BufferedImage
     *
     * @param base64ImageUrl Base64 encoded image
     * @return the BufferedImage
     * @throws IOException
     */
    public static BufferedImage parseBase64ToBufferedImage(String base64ImageUrl) throws IOException {
        String base64ImageString;
        // quick and dirty data url split 'cause regex can be troublesome lol
        // illegal base64 will cause IOException when calling Base64.getDecoder().decode()
        // and unsupported image media type will cause BufferedImage related exception
        // both can be caught & handled by the controller so shouldn't be a problem?
        if (base64ImageUrl.startsWith("data:") && base64ImageUrl.contains(",")) {
            base64ImageString = base64ImageUrl.substring(base64ImageUrl.indexOf(",") + 1);
        } else {
            throw new IOException("Illegal Base64 Data URL");
        }
        byte[] imageBytes = Base64.getDecoder().decode(base64ImageString.getBytes());
        return ImageIO.read(new ByteArrayInputStream(imageBytes));
    }
}
