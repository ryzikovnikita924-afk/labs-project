package com.example.repository;

import com.example.dto.Hotels;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RegisterConstructorMapper(Hotels.class)
public interface HotelsDao {

    @SqlQuery("SELECT * FROM hotels WHERE id = :id")
    Optional<Hotels> getById(@Bind("id") UUID id);

    @SqlQuery("SELECT * FROM hotels ORDER BY name")
    List<Hotels> getAll();

    @SqlUpdate("INSERT INTO hotels (id, name, stars) VALUES (:id, :name, :stars)")
    void insert(@Bind("id") UUID id, @Bind("name") String name, @Bind("stars") int stars);

    @SqlQuery("SELECT * FROM hotels WHERE name = :name")
    Optional<Hotels> findByName(@Bind("name") String name);

    @SqlUpdate("UPDATE hotels SET name = :name, stars = :stars WHERE id = :id")
    int update(@Bind("id") UUID id, @Bind("name") String name, @Bind("stars") int stars);

    @SqlUpdate("DELETE FROM hotels WHERE id = :id")
    void deleteById(@Bind("id") UUID id);
}