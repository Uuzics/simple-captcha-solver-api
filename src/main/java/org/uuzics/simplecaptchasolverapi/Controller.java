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
package org.uuzics.simplecaptchasolverapi;

import org.uuzics.simplecaptchasolverapi.util.processor.OcrProcessor;
import org.uuzics.simplecaptchasolverapi.util.processor.PreOcrProcessor;
import org.uuzics.simplecaptchasolverapi.util.processor.RawImageProcessor;
import org.uuzics.simplecaptchasolverapi.util.request.Base64CaptchaRequest;
import org.uuzics.simplecaptchasolverapi.util.response.SimpleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller for Spring Boot API
 */
@RestController
public class Controller {
    @Autowired
    private Configuration configuration;

    /**
     * Solve a Base64 encoded captcha
     *
     * @param request the request containing Base64 encoded captcha image
     * @return SimpleResponse
     */
    @RequestMapping(value = "/solveBase64Captcha", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public SimpleResponse solveBase64Captcha(@RequestBody Base64CaptchaRequest request) {
        String base64ImageString = request.getBase64();

        int length_expected = this.configuration.getApi().getLength_expected();
        boolean fail_if_length_unexpected = this.configuration.getApi().isFail_if_length_unexpected();

        String text;
        try {
            BufferedImage rawImage = RawImageProcessor.parseBase64ToBufferedImage(base64ImageString);
            BufferedImage preProcessedImage = PreOcrProcessor.doPreOcrProcess(rawImage, this.configuration);
            text = OcrProcessor.doOcrProcess(preProcessedImage, this.configuration);
            // Remove unwanted chars
            Pattern pattern = Pattern.compile("\\s*|\t|\r|\n");
            Matcher matcher = pattern.matcher(text);
            text = matcher.replaceAll("");
        } catch (IOException e) {
            return new SimpleResponse(false, "");
        }
        if (text != null) {
            if (!fail_if_length_unexpected || text.length() == length_expected) {
                return new SimpleResponse(true, text);
            } else {
                return new SimpleResponse(false, text);
            }
        } else {
            return new SimpleResponse(false, "");
        }
    }
}
