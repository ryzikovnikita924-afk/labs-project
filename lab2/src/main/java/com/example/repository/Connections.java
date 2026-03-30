package com.example.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Connections {
    private final DataSource dataSource;

    public Connections(String url, String user, String password, String schema) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");


        config.setMaximumPoolSize(10); // Максимальное количество соединений в пуле
        config.setMinimumIdle(5); // Минимальное количество простаивающих соединений
        config.setIdleTimeout(300000); // Время простоя соединения (в миллисекундах, 5 минут)
        config.setMaxLifetime(1800000); // Максимальное время жизни соединения (30 минут)
        config.setConnectionTimeout(30000); // Таймаут ожидания соединения (30 секунд)

        config.setSchema(schema);

        // Инициализация пула
        dataSource = new HikariDataSource(config);

        // чтобы избежать утечек ресурсов БД (!!!), закрываем пул соединений, когда приложение завершает работу
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeDataSource));
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    // Метод для закрытия пула (вызывать при завершении приложения)
    public void closeDataSource() {
        if (dataSource != null && dataSource instanceof HikariDataSource hikariDataSource) {
            hikariDataSource.close();
        }
    }

}