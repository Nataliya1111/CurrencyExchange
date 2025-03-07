package com.nataliya1111.dao;

import com.nataliya1111.entity.ExchangeRate;
import com.nataliya1111.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDao {

    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();

    private static final String GET_ALL_SQL = """
            SELECT id,
               base_currency_id,
               target_currency_id,
               rate
            FROM ExchangeRates       
            """;

    private ExchangeRatesDao(){
    }

    public static ExchangeRatesDao getInstance(){
        return INSTANCE;
    }

    public List<ExchangeRate> getAll(){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ExchangeRate> exchangeRatesList = new ArrayList<>();
            while (resultSet.next()){
                ExchangeRate exchangeRate = buildExchangeRate(resultSet);
                exchangeRatesList.add(exchangeRate);
            }
            return exchangeRatesList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ExchangeRate buildExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(resultSet.getLong("id"),
                resultSet.getLong("base_currency_id"),
                resultSet.getLong("target_currency_id"),
                resultSet.getBigDecimal("rate")
        );
    }
}
