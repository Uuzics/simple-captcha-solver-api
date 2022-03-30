package org.uuzics.simplecaptchasolverapi;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
        private long request_size_limit_bytes;

        public Api() {
            this.length_expected = 4;
            this.fail_if_length_unexpected = false;
            this.request_size_limit_bytes = 1000;
        }
    }

    @Data
    public class Ocr {
        private double threshold_thresh;
        private double threshold_maxval;
        private int erode_element_size;

        private String tess_data;
        private String tess_lang;

        public Ocr() {
            this.threshold_thresh = 130;
            this.threshold_maxval = 255;
            this.erode_element_size = 2;
        }
    }
}
