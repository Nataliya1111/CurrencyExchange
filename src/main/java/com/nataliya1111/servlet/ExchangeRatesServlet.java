package com.nataliya1111.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya1111.dao.CurrencyDao;
import com.nataliya1111.dto.ExchangeRateRequestDto;
import com.nataliya1111.dto.ExchangeRateResponseDto;
import com.nataliya1111.entity.Currency;
import com.nataliya1111.entity.ExchangeRate;
import com.nataliya1111.exception.DataExistsException;
import com.nataliya1111.exception.InvalidRequestException;
import com.nataliya1111.service.ExchangeRatesService;
import com.nataliya1111.util.RequestValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private static ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();
    private static CurrencyDao currencyDao = CurrencyDao.getInstance();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<ExchangeRateResponseDto> exchangeRatesList = exchangeRatesService.getAll();
        objectMapper.writeValue(resp.getWriter(), exchangeRatesList);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        if (RequestValidator.isExchangeRateParametersMissed(baseCurrencyCode, targetCurrencyCode, rate)){
            throw new InvalidRequestException("Missing required currency data");
        }
        if (!RequestValidator.isCurrencyCodeValid(baseCurrencyCode) || !RequestValidator.isCurrencyCodeValid(targetCurrencyCode) || !RequestValidator.isRateValid(rate)){
            throw new InvalidRequestException("Invalid exchange rate data. Base and target currency code: 3 uppercase Latin letters. Rate: positive number no longer than 10 characters, like '123.45' or '25'");
        }

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, rate);
        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRatesService.add(exchangeRateRequestDto);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), exchangeRateResponseDto);
    }
}
