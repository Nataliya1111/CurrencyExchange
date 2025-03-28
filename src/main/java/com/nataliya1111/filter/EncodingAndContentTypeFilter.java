package com.nataliya1111.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter(value = {
           "/currencies", "/currency/*", "/exchangeRates", "/exchangeRate/*", "/exchange"
})
public class EncodingAndContentTypeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        servletRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
        servletResponse.setCharacterEncoding((StandardCharsets.UTF_8.name()));
        servletResponse.setContentType("application/json");

        filterChain.doFilter(servletRequest, servletResponse);

    }
}
