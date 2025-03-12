package com.nataliya1111.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya1111.dao.CurrencyDao;
import com.nataliya1111.entity.Currency;
import com.nataliya1111.util.RequestValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private static CurrencyDao currencyDao = CurrencyDao.getInstance();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Currency> currenciesList = currencyDao.getAll();
        objectMapper.writeValue(resp.getWriter(), currenciesList);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String sign = req.getParameter("sign");

        if (code.isBlank() || code == null || name.isBlank() || name == null || sign.isBlank() || sign == null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);   //400
            resp.getWriter().write("Missing required currency data");
            return;
        }

        if (!RequestValidator.isCurrencyCodeValid(code) || !RequestValidator.isCurrencyNameValid(name) || !RequestValidator.isCurrencySignValid(sign)){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);   //400
            resp.getWriter().write("Invalid currency data. Name: up to 30 Latin letters. Code: 3 uppercase Latin letters. Sign: up to 3 characters");
            return;
        }

        if (currencyDao.getByCode(code).isPresent()){
            resp.setStatus(HttpServletResponse.SC_CONFLICT);   //409
            resp.getWriter().write("Currency with such code already exists");
            return;
        }

        Currency newCurrency = new Currency();
        newCurrency.setCode(code);
        newCurrency.setFullName(name);
        newCurrency.setSign(sign);

        Currency addedCurrency = currencyDao.add(newCurrency);

        objectMapper.writeValue(resp.getWriter(), addedCurrency);

    }
}
