package com.nataliya1111.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya1111.dao.CurrencyDao;
import com.nataliya1111.entity.Currency;
import com.nataliya1111.exception.DataExistsException;
import com.nataliya1111.exception.InvalidRequestException;
import com.nataliya1111.util.RequestValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        List<Currency> currenciesList = currencyDao.getAll();
        objectMapper.writeValue(resp.getWriter(), currenciesList);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        if (RequestValidator.isCurrencyParametersMissed(code, name, sign)){
            throw new InvalidRequestException("Missing required currency data");
        }

        if (!RequestValidator.isCurrencyCodeValid(code) || !RequestValidator.isCurrencyNameValid(name) || !RequestValidator.isCurrencySignValid(sign)){
            throw new InvalidRequestException("Invalid currency data. Name: up to 30 Latin letters. Code: 3 uppercase Latin letters. Sign: up to 3 characters");
        }

        if (currencyDao.getByCode(code).isPresent()){
            throw new DataExistsException("Currency with such code already exists");
        }

        Currency newCurrency = new Currency();
        newCurrency.setCode(code);
        newCurrency.setFullName(name);
        newCurrency.setSign(sign);

        Currency addedCurrency = currencyDao.add(newCurrency);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), addedCurrency);
    }
}
