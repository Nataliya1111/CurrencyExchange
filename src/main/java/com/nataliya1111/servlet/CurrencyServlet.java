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
import java.util.NoSuchElementException;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyDao currencyDao = CurrencyDao.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String currencyCode = req.getPathInfo().replaceFirst("/", "");

        if (!RequestValidator.isCurrencyCodeValid(currencyCode)){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);    //400
            resp.getWriter().write("Code of currency is invalid or missed. Code must contain 3 uppercase letters");
            return;
        }

        Optional<Currency> currency = currencyDao.getByCode(currencyCode);

        Currency searchedCurrency = null;
        try {
            searchedCurrency = currency.get();
        } catch (NoSuchElementException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);    //404
            resp.getWriter().write("Currency is not found");
            return;
        }

        objectMapper.writeValue(resp.getWriter(), searchedCurrency);
    }
}
