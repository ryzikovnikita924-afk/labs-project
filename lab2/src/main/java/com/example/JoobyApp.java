package com.example;
import com.example.repository.HotelControllerJooby;
import com.example.dto.UniversalResponse;
import com.example.exceptions.BaseException;
import com.example.repository.*;
import io.jooby.handler.AccessLogHandler;
import io.jooby.jackson.JacksonModule;
import io.jooby.Jooby;
import io.jooby.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class JoobyApp extends Jooby {
    private static final Logger log = LoggerFactory.getLogger(JoobyApp.class);

    public JoobyApp() {
        install(new JacksonModule());
        use(new AccessLogHandler());

        String databaseUrl = System.getenv("DATABASE_URL");
        String databaseUser = System.getenv("DATABASE_USERNAME");
        String databasePassword = System.getenv("DATABASE_PASSWORD");
        String databaseSchema = System.getenv("APP_DATABASE_SCHEMA");

        if (databaseUrl == null || databaseUser == null || databasePassword == null || databaseSchema == null) {
            log.error("Не заданы переменные окружения для подключения к БД!");
            throw new RuntimeException("Не заданы переменные окружения для подключения к БД!");
        }

        Connections connectionFactory = new Connections(databaseUrl, databaseUser, databasePassword, databaseSchema);
        DataSource dataSource = connectionFactory.getDataSource();

        DatabaseMigrator databaseMigrator = new DatabaseMigrator(connectionFactory);
        databaseMigrator.runMigrations();

        HotelsRepository hotelsRepository = new HotelsRepository(dataSource);
        HotelControllerJooby hotelController = new HotelControllerJooby(hotelsRepository);

        setupRoutes(hotelController);

        error(BaseException.class, (ctx, cause, statusCode) -> {
            log.error("Business error: ", cause);
            BaseException e = (BaseException) cause;
            ctx.setResponseCode(StatusCode.valueOf(e.getHttpCode()));
            ctx.render(new UniversalResponse<>(e.getCode(), e.getMessage()));
        });

        error(Exception.class, (ctx, cause, statusCode) -> {
            log.error("Unexpected error: ", cause);
            ctx.setResponseCode(StatusCode.SERVER_ERROR);
            ctx.render(new UniversalResponse<>(5000, "Неизвестная ошибка: " + cause.getMessage()));
        });
    }

    private void setupRoutes(HotelControllerJooby hotelController) {
        get("/Hotels", hotelController::getAllHotels);
        get("/hotels_get/{id}", hotelController::getHotelById);
        post("/hotels_cr", hotelController::createHotel);
        put("/hotels_up/{id}", hotelController::updateHotel);
        delete("/hotels_del/{id}", hotelController::deleteHotel);
        get("/demo", hotelController::demoCrud);
    }
}