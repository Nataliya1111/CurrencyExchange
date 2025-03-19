package com.nataliya1111.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya1111.exception.DataExistsException;
import com.nataliya1111.exception.DataNotFoundException;
import com.nataliya1111.exception.DatabaseException;
import com.nataliya1111.exception.InvalidRequestException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebFilter("/*")
public class JsonErrorHandlingFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try{
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (InvalidRequestException e) {   //400
            sendJsonErrorResponse((HttpServletResponse) servletResponse, HttpServletResponse.SC_BAD_REQUEST, e);
        } catch (DataNotFoundException e) {   //404
            sendJsonErrorResponse((HttpServletResponse) servletResponse, HttpServletResponse.SC_NOT_FOUND, e);
        } catch (DataExistsException e) {   //409
            sendJsonErrorResponse((HttpServletResponse) servletResponse, HttpServletResponse.SC_CONFLICT, e);
        } catch (DatabaseException e) {   //500
            sendJsonErrorResponse((HttpServletResponse) servletResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
        }

    }

    private void sendJsonErrorResponse (HttpServletResponse response, int statusCode, RuntimeException e) throws IOException {
        Map<String, String> ErrorResponse = Map.of("message", e.getMessage());

        response.setStatus(statusCode);
        objectMapper.writeValue(response.getWriter(), ErrorResponse);
    }



}
