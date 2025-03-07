package com.nataliya1111.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nataliya1111.dao.ExchangeRatesDao;
import com.nataliya1111.dto.ExchangeRatesDto;
import com.nataliya1111.entity.ExchangeRate;
import com.nataliya1111.service.ExchangeRatesService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    private static ExchangeRatesService exchangeRatesService = ExchangeRatesService.getInstance();
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<ExchangeRatesDto> exchangeRatesList = exchangeRatesService.getAll();
        objectMapper.writeValue(resp.getWriter(), exchangeRatesList);


    }
}
