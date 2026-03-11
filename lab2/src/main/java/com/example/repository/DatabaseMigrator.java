package com.example.repository;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class DatabaseMigrator {

    private static final Logger log = LoggerFactory.getLogger(DatabaseMigrator.class);
    private final Connections connections;

    public DatabaseMigrator(Connections connections) {
        this.connections = connections;
    }

    public void runMigrations() {
        System.setProperty("liquibase.secureParsing", "false");
        try (Connection connection = connections.getConnection()) {

            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    new JdbcConnection(connection)
            );

            liquibase.update();
            System.out.println("Миграции успешно применены!");
        } catch (Exception e) {
            log.error("Ошибка применения миграций БД: ", e);
            throw new RuntimeException("Ошибка при выполнении миграций", e);
        }
    }
}