package com.nataliya1111.dao;

import com.nataliya1111.entity.Currency;
import com.nataliya1111.exception.DaoException;
import com.nataliya1111.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class CurrencyDao {

    private static final CurrencyDao INSTANCE = new CurrencyDao();

    private static final String ADD_SQL = """
            INSERT INTO Currencies(code, full_name, sign) 
            VALUES (?, ?, ?);            
            """;

    private static final String FIND_ALL_SQL = """
            SELECT id, 
                code, 
                full_name, 
                sign
            FROM Currencies
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM Currencies
            WHERE id = ?
            """;

    private CurrencyDao() {
    }

    public Currency add(Currency currency){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(ADD_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, currency.getCode());
            preparedStatement.setString(2, currency.getFullName());
            preparedStatement.setString(3, currency.getSign());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                currency.setId(generatedKeys.getLong(1));
            }
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Currency> findById(Long id){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Currency currency = null;
            if (resultSet.next()){
                currency = buildCurrency(resultSet);
            }
            return Optional.ofNullable(currency);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Currency> findAll(){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Currency> listOfCurrencies = new ArrayList<>();
            while (resultSet.next()){
                Currency currency = buildCurrency(resultSet);
                listOfCurrencies.add(currency);
            }
            return listOfCurrencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("");
        }
    }

    public static CurrencyDao getInstance(){
        return INSTANCE;
    }

    private static Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }



}
