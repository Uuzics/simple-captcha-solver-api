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
package org.uuzics.simplecaptchasolverapi.util.response;

import lombok.Getter;
import lombok.Setter;

/**
 * SimpleResponse
 * <br>
 * Response including only status and solution
 */
public class SimpleResponse {
    @Getter @Setter private boolean success;
    @Getter @Setter private String text;
    @Getter @Setter private int confidence;

    public SimpleResponse() {
    }

    public SimpleResponse(boolean success, String text, int confidence) {
        this.success = success;
        this.text = text;
        this.confidence = confidence;
    }
}
