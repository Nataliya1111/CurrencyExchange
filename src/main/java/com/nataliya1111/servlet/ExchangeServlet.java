package com.nataliya1111.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya1111.dto.ExchangeRequestDto;
import com.nataliya1111.dto.ExchangeResponseDto;
import com.nataliya1111.exception.InvalidRequestException;
import com.nataliya1111.service.ExchangeService;
import com.nataliya1111.util.RequestValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeService exchangeService = ExchangeService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (RequestValidator.isExchangeParametersMissed(baseCurrencyCode, targetCurrencyCode, amount)){
            throw new InvalidRequestException("Missing required data: 'Base currency code', 'Target currency code', and 'Amount' parameters are required");
        }

        if (!RequestValidator.isCurrencyCodeValid(baseCurrencyCode) || !RequestValidator.isCurrencyCodeValid(targetCurrencyCode)){
            throw new InvalidRequestException("At list one currency code is invalid. Code must contain 3 uppercase Latin letters");
        }

        if (!RequestValidator.isAmountValid(amount)){
            throw new InvalidRequestException("Amount is invalid. Amount must be a positive number no longer than 16 characters");
        }

        if (RequestValidator.isCodesEqual(baseCurrencyCode, targetCurrencyCode)){
            throw new InvalidRequestException("Codes can't be equal");
        }

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(baseCurrencyCode, targetCurrencyCode, new BigDecimal(amount));
        ExchangeResponseDto exchangeResponseDto = exchangeService.exchange(exchangeRequestDto);

        objectMapper.writeValue(resp.getWriter(), exchangeResponseDto);
    }
}
