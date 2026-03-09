package com.example;

import dto.HotelInitialization;
import dto.UniversalResponse;
import exceptions.BaseException;
import io.jooby.handler.AccessLogHandler;
import io.jooby.jackson.JacksonModule;
import repository.Connections;
import repository.DatabaseMigrator;
import repository.HotelsRepository;
import io.jooby.Jooby;
import io.jooby.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.UUID;

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

        setupHotelRoutes(hotelsRepository);

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

    private void setupHotelRoutes(HotelsRepository hotelsRepository) {
        get("/hotels", ctx -> {
            log.info("GET /hotels - получение всех отелей");
            return hotelsRepository.getAll();
        });

        get("/hotels/{id}", ctx -> {
            String id = ctx.path("id").value();
            log.info("GET /hotels/{} - получение отеля по ID", id);
            return hotelsRepository.getById(UUID.fromString(id))
                    .orElseThrow(() -> new BaseException(404, 4041, "Отель не найден"));
        });

        post("/hotels", ctx -> {
            log.info("POST /hotels - создание нового отеля");
            HotelInitialization request = ctx.body(HotelInitialization.class);

            UUID id = UUID.randomUUID();
            hotelsRepository.insert(id, request.getName(), request.getStars());

            return hotelsRepository.getById(id)
                    .orElseThrow(() -> new BaseException(500, 5001, "Ошибка при создании отеля"));
        });

        put("/hotels/{id}", ctx -> {
            String id = ctx.path("id").value();
            log.info("PUT /hotels/{} - обновление отеля", id);

            HotelInitialization request = ctx.body(HotelInitialization.class);

            boolean updated = hotelsRepository.update(
                    UUID.fromString(id),
                    request.getName(),
                    request.getStars()
            );

            if (!updated) {
                throw new BaseException(404, 4041, "Отель не найден");
            }

            return hotelsRepository.getById(UUID.fromString(id))
                    .orElseThrow(() -> new BaseException(500, 5002, "Ошибка при обновлении отеля"));
        });

        delete("/hotels/{id}", ctx -> {
            String id = ctx.path("id").value();
            log.info("DELETE /hotels/{} - удаление отеля", id);

            hotelsRepository.deleteById(UUID.fromString(id));

            return new UniversalResponse<>(200, "Отель успешно удален");
        });

        get("/demo", ctx -> {
            StringBuilder result = new StringBuilder();
            result.append("=== ДЕМОНСТРАЦИЯ CRUD ОПЕРАЦИЙ ===\n\n");

            try {
                UUID id1 = UUID.randomUUID();
                UUID id2 = UUID.randomUUID();
                UUID id3 = UUID.randomUUID();

                hotelsRepository.insert(id1, "Grand Hotel", 5);
                result.append("Создан отель: Grand Hotel (5 звезд), id=").append(id1).append("\n");

                hotelsRepository.insert(id2, "City Inn", 3);
                result.append("Создан отель: City Inn (3 звезды), id=").append(id2).append("\n");

                hotelsRepository.insert(id3, "Budget Stay", 2);
                result.append("Создан отель: Budget Stay (2 звезды), id=").append(id3).append("\n\n");

                result.append("Все отели:\n");
                hotelsRepository.getAll().forEach(hotel ->
                        result.append("  ").append(hotel).append("\n")
                );
                result.append("\n");

                result.append("Поиск отеля по id=").append(id1).append("\n");
                var hotel1 = hotelsRepository.getById(id1);
                hotel1.ifPresent(h -> result.append("Найден: ").append(h).append("\n\n"));

                result.append("Обновление отеля id=").append(id1).append("\n");
                boolean updated = hotelsRepository.update(id1, "Grand Hotel Luxury", 5);
                if (updated) {
                    result.append("Отель обновлен\n");
                    var updatedHotel = hotelsRepository.getById(id1);
                    updatedHotel.ifPresent(h -> result.append("После обновления: ").append(h).append("\n"));
                }
                result.append("\n");

                result.append("Попытка обновить несуществующий отель\n");
                UUID fakeId = UUID.randomUUID();
                boolean updateFake = hotelsRepository.update(fakeId, "Fake Hotel", 5);
                result.append("Результат: ").append(updateFake).append("\n\n");

                result.append("Удаление отеля id=").append(id2).append("\n");
                hotelsRepository.deleteById(id2);
                result.append("Отель удален\n");
                result.append("Проверка: отель ").append(
                        hotelsRepository.getById(id2).isEmpty() ? "не существует" : "существует"
                ).append("\n\n");

                result.append("Удаление несуществующего отеля\n");
                hotelsRepository.deleteById(fakeId);
                result.append("Удаление выполнено без ошибок\n\n");

                result.append("Финальный список отелей:\n");
                hotelsRepository.getAll().forEach(hotel ->
                        result.append("  ").append(hotel).append("\n")
                );

            } catch (Exception e) {
                result.append("Ошибка: ").append(e.getMessage());
                log.error("Ошибка в демонстрации CRUD", e);
            }

            return result.toString();
        });
    }
}