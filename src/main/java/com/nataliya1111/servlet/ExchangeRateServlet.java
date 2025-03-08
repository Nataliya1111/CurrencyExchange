package com.nataliya1111.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya1111.dto.ExchangeRateDto;
import com.nataliya1111.exception.NoSuchCurrencyException;
import com.nataliya1111.exception.NoSuchExchangeRateException;
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
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String codesPair = req.getPathInfo().replaceFirst("/", "");

        if (!RequestValidator.isCodesPairValid(codesPair)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);    //400
            resp.getWriter().write("Invalid request. There must be two codes of 3 uppercase letters in a row");
            return;
        }

        ExchangeRateDto exchangeRateDto;
        try {
            exchangeRateDto = exchangeRatesService.getByCodes(codesPair);
        } catch (NoSuchCurrencyException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);    //400
            resp.getWriter().write("Invalid request. One or two currencies are not found");
            return;
        } catch (NoSuchExchangeRateException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);    //404
            resp.getWriter().write("Exchange rate is not found");
            return;
        }

        objectMapper.writeValue(resp.getWriter(), exchangeRateDto);
    }
}
