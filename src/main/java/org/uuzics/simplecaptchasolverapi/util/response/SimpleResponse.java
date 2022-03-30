package org.uuzics.simplecaptchasolverapi.util.response;

import lombok.Getter;
import lombok.Setter;

public class SimpleResponse {
    @Getter @Setter private boolean success;
    @Getter @Setter private String text;

    public SimpleResponse() {
    }

    public SimpleResponse(boolean success, String text) {
        this.success = success;
        this.text = text;
    }
}
