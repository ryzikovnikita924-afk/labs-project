package com.example.repository;

import com.example.dto.hotels;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class HotelsMapper implements RowMapper<hotels> {
    @Override
    public hotels map(ResultSet rs, StatementContext ctx) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String name = rs.getString("name");
        int stars = rs.getInt("stars");
        return new hotels(id, name, stars);
    }
}
