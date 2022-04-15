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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration for captcha processors and API
 */
@Data
@Component
@ConfigurationProperties(prefix = "custom-configuration")
public class Configuration {
    private Api api;
    private Ocr ocr;

    public Configuration() {
        this.api = new Api();
        this.ocr = new Ocr();
    }

    @Data
    public class Api {
        private int length_expected;
        private boolean fail_if_length_unexpected;

        private int minimum_confidence;
        private boolean fail_if_unconfident;

        private long request_size_limit_bytes;

        public Api() {
            this.length_expected = 4;
            this.fail_if_length_unexpected = false;

            this.minimum_confidence = 90;
            this.fail_if_unconfident = true;

            this.request_size_limit_bytes = 4096;
        }
    }

    @Data
    public class Ocr {
        private double threshold_thresh;
        private double threshold_maxval;
        private int erode_element_size;
        private int resize_size;

        private String tess_data;
        private String tess_lang;
        private int tess_psm;

        public Ocr() {
            this.threshold_thresh = 130;
            this.threshold_maxval = 255;
            this.erode_element_size = 2;
            this.resize_size = 1;

            this.tess_psm = 7;
        }
    }
}
