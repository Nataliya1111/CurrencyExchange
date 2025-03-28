package com.nataliya1111.dao;

import com.nataliya1111.entity.Currency;
import com.nataliya1111.exception.DatabaseException;
import com.nataliya1111.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

    private static final CurrencyDao INSTANCE = new CurrencyDao();

    private static final String ADD_SQL = """
            INSERT INTO Currencies(code, full_name, sign) 
            VALUES (?, ?, ?);            
            """;

    private static final String GET_ALL_SQL = """
            SELECT id, 
                code, 
                full_name, 
                sign
            FROM Currencies
            """;

    private static final String GET_BY_CODE_SQL = GET_ALL_SQL + """
            WHERE code = ?
            """;

    private static final String GET_BY_ID_SQL = GET_ALL_SQL + """
            WHERE id = ?
            """;

    private CurrencyDao() {
    }

    public static CurrencyDao getInstance(){
        return INSTANCE;
    }

    public Currency add(Currency currency){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getName());
            preparedStatement.setString(3, currency.getSign());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                currency.setId(generatedKeys.getLong(1));
            }
            return currency;
        } catch (SQLException e) {
            throw new DatabaseException("Database error: Unable to add currency");
        }
    }

    public Optional<Currency> getByCode(String code){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_CODE_SQL)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            Currency currency = null;
            if (resultSet.next()){
                currency = buildCurrency(resultSet);
            }
            return Optional.ofNullable(currency);

        } catch (SQLException e) {
            throw new DatabaseException("Database error: Unable to read currency");
        }
    }

    public Optional<Currency> getById(Long id){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Currency currency = null;
            if (resultSet.next()){
                currency = buildCurrency(resultSet);
            }
            return Optional.ofNullable(currency);

        } catch (SQLException e) {
            throw new DatabaseException("Database error: Unable to read currency");
        }
    }

    public List<Currency> getAll(){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> currenciesList = new ArrayList<>();
            while (resultSet.next()){
                Currency currency = buildCurrency(resultSet);
                currenciesList.add(currency);
            }
            return currenciesList;
        } catch (SQLException e) {
            throw new DatabaseException("Database error: Unable to read currencies");
        }

    }

    private static Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
