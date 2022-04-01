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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RequestSizeLimiter extends OncePerRequestFilter
 * <br>
 * Limits size on each request before captcha solving
 */
@Component
public class RequestSizeLimiter extends OncePerRequestFilter {
    @Autowired
    private Configuration configuration;

    /**
     * Overrides doFilterInternal
     * <br>
     * Throws IOException when request size exceeded limit
     *
     * @param request     HttpServletRequest
     * @param response    HttpServletResponse
     * @param filterChain FilterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long request_size_limit_bytes = configuration.getApi().getRequest_size_limit_bytes();
        if (request.getContentLengthLong() > request_size_limit_bytes) {
            throw new IOException("Request size exceeded limit of " + request_size_limit_bytes + " bytes");
        }
        filterChain.doFilter(request, response);
    }
}
