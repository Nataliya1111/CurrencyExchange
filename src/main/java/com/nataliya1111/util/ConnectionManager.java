package com.nataliya1111.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String URL_KEY = "db.url";
    private static final String DRIVER_KEY = "db.driver";
    private static final HikariDataSource HIKARI_DATA_SOURCE;

    private ConnectionManager(){
    }

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PropertiesUtil.get(URL_KEY));
        config.setDriverClassName(PropertiesUtil.get(DRIVER_KEY));
        HIKARI_DATA_SOURCE = new HikariDataSource(config);
    }

    public static Connection get() {
        try {
            return HIKARI_DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Database is unavailable");
        }
    }

}
