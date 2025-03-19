package com.nataliya1111.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya1111.dao.CurrencyDao;
import com.nataliya1111.entity.Currency;
import com.nataliya1111.exception.DataNotFoundException;
import com.nataliya1111.exception.InvalidRequestException;
import com.nataliya1111.util.RequestValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String currencyCode = req.getPathInfo().replaceFirst("/", "");

        if (!RequestValidator.isCurrencyCodeValid(currencyCode)){
            throw new InvalidRequestException("Code of currency is invalid or missed. Code must contain 3 uppercase Latin letters");
        }

        Optional<Currency> currency = currencyDao.getByCode(currencyCode);

        Currency searchedCurrency;
        try {
            searchedCurrency = currency.get();
        } catch (NoSuchElementException e) {
            throw new DataNotFoundException("Currency with such code is not found");
        }

        objectMapper.writeValue(resp.getWriter(), searchedCurrency);
    }
}
