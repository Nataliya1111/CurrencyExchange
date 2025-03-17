package com.nataliya1111.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya1111.dto.ExchangeRateRequestDto;
import com.nataliya1111.dto.ExchangeRateResponseDto;
import com.nataliya1111.exception.InvalidRequestException;
import com.nataliya1111.service.ExchangeRatesService;
import com.nataliya1111.util.RequestValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private static ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    private void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String codesPair = req.getPathInfo().replaceFirst("/", "");
        BufferedReader reader = req.getReader();
        String requestBody = reader.readLine();
        String[] parameters = requestBody.split("&");
        String newRate = null;
        for (String parameter : parameters){
            if (parameter.startsWith("rate=")){
                newRate = parameter.substring(5);
                break;
            }
        }

        if (newRate  == null || newRate.isBlank()){
            throw new InvalidRequestException("Missing rate data");   //400
        }

        if (!RequestValidator.isCodesPairValid(codesPair)) {   //400
            throw new InvalidRequestException("Invalid request. Expected two codes of 3 uppercase letters (example USDRUB)");
        }
        if (RequestValidator.isCodesInPairEquals(codesPair)){   //400
            throw new InvalidRequestException("Invalid request. Codes can't be equal");
        }
        if (!RequestValidator.isRateValid(newRate)){   //400
            throw new InvalidRequestException("Invalid exchange rate data. Rate must be positive number no longer than 10 characters, like '123.45' or '25'");
        }

        String baseCurrencyCode = codesPair.substring(0, 3);
        String targetCurrencyCode = codesPair.substring(3);
        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, newRate);
        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.updateExchangeRate(exchangeRateRequestDto);

        objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String codesPair = req.getPathInfo().replaceFirst("/", "");

        if (!RequestValidator.isCodesPairValid(codesPair)) {   //400
            throw new InvalidRequestException("Invalid request. Expected two codes of 3 uppercase letters (example USDRUB)");
        }
        if (RequestValidator.isCodesInPairEquals(codesPair)){   //400
            throw new InvalidRequestException("Invalid request. Codes can't be equal");
        }

        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.getByCodes(codesPair);

        objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto);
    }


}