package com.nataliya1111.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya1111.dto.ExchangeRateResponseDto;
import com.nataliya1111.exception.InvalidRequestException;
import com.nataliya1111.service.ExchangeRatesService;
import com.nataliya1111.util.RequestValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private static ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String codesPair = req.getPathInfo().replaceFirst("/", "");

        if (!RequestValidator.isCodesPairValid(codesPair)) {   //400
            throw new InvalidRequestException("Invalid request. Expected two codes of 3 uppercase letters (example USDRUB)");
        }
        if (RequestValidator.isCodesInPairEquals(codesPair)){   //400
            throw new InvalidRequestException("Codes can't be equal");
        }

        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.getByCodes(codesPair);

        objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto);
    }
}