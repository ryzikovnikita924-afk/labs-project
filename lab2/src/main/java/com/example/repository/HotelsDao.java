package com.example.repository;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.example.dto.hotels;

    @RegisterRowMapper(HotelsMapper.class)
    public interface HotelsDao {

        @SqlQuery("SELECT id, name, stars FROM hotels WHERE id = :id")
        Optional<hotels> getById(@Bind("id") UUID id);

        @SqlQuery("SELECT id, name, stars FROM hotels ORDER BY name, id")
        List<hotels> getAll();

        @SqlUpdate("INSERT INTO hotels (id, name, stars) VALUES (:id, :name, :stars)")
        void insert(@Bind("id") UUID id,
                    @Bind("name") String name,
                    @Bind("stars") int stars);

        @SqlUpdate("UPDATE hotels SET name = :name, stars = :stars WHERE id = :id")
        int update(@Bind("id") UUID id,
                   @Bind("name") String name,
                   @Bind("stars") int stars);

        @SqlUpdate("DELETE FROM hotels WHERE id = :id")
        void deleteById(@Bind("id") UUID id);
    }


