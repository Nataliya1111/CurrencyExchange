package com.nataliya1111.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya1111.dao.CurrencyDao;
import com.nataliya1111.entity.Currency;
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
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<Currency> currenciesList = currencyDao.getAll();
        objectMapper.writeValue(resp.getWriter(), currenciesList);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Currency newCurrency = new Currency();
        newCurrency.setCode(req.getParameter("code"));
        newCurrency.setFullName(req.getParameter("name"));
        newCurrency.setSign(req.getParameter("sign"));

        Currency addedCurrency = currencyDao.add(newCurrency);

        objectMapper.writeValue(resp.getWriter(), addedCurrency);

    }
}
