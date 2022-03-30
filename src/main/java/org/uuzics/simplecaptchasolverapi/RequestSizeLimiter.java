package org.uuzics.simplecaptchasolverapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestSizeLimiter extends OncePerRequestFilter {
    @Autowired
    private Configuration configuration;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long request_size_limit_bytes = configuration.getApi().getRequest_size_limit_bytes();
        if (request.getContentLengthLong() > request_size_limit_bytes) {
            throw new IOException("Request size exceeded limit of " + request_size_limit_bytes + " bytes");
        }
        filterChain.doFilter(request, response);
    }
}
