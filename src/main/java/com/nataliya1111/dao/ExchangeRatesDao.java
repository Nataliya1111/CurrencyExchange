package com.nataliya1111.dao;

import com.nataliya1111.entity.ExchangeRate;
import com.nataliya1111.exception.DatabaseException;
import com.nataliya1111.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRatesDao {

    private static final ExchangeRatesDao INSTANCE = new ExchangeRatesDao();

    private static final String ADD_SQL = """
            INSERT INTO ExchangeRates(base_currency_id, target_currency_id, rate)
            VALUES (?, ?, ?);
            """;

    private static final String GET_ALL_SQL = """
            SELECT id,
               base_currency_id,
               target_currency_id,
               rate
            FROM ExchangeRates       
            """;

    private static final String GET_BY_BASE_AND_TARGET_ID = GET_ALL_SQL + """
            WHERE base_currency_id = ? AND target_currency_id = ?
            """;

    private ExchangeRatesDao(){
    }

    public static ExchangeRatesDao getInstance(){
        return INSTANCE;
    }

    public ExchangeRate add(ExchangeRate exchangeRate){
        try (Connection connection = ConnectionManager.get();
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL)) {
            preparedStatement.setLong(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setLong(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                exchangeRate.setId(generatedKeys.getLong(1));
            }
            return exchangeRate;
        } catch (SQLException e) {
            throw new DatabaseException("Database error: Unable to add exchange rate");
        }
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
            throw new DatabaseException("Database error: Unable to read exchange rates");
        }
    }

    public Optional<ExchangeRate> getByBaseAndTargetId(Long baseCurrencyId, Long targetCurrencyId){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_BASE_AND_TARGET_ID)) {
            preparedStatement.setLong(1, baseCurrencyId);
            preparedStatement.setLong(2, targetCurrencyId);

            ResultSet resultSet = preparedStatement.executeQuery();
            ExchangeRate exchangeRate = null;

            if(resultSet.next()){
                exchangeRate = buildExchangeRate(resultSet);
            }
            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DatabaseException("Database error: Unable to read exchange rate");
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
