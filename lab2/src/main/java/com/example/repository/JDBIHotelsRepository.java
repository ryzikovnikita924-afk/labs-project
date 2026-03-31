package com.example.repository;

import com.example.dto.Hotels;
import com.example.exceptions.RepositoryException;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JDBIHotelsRepository implements HotelRepository {
    private final HotelsDao dao;

    public JDBIHotelsRepository(DataSource dataSource) {
        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(HotelsDao.class);
    }

    @Override
    public Optional<Hotels> getById(UUID id) {
        try {
            return dao.getById(id);
        } catch (Exception e) {
            throw new RepositoryException("Failed to fetch Hotels by id", e);
        }
    }

    @Override
    public Optional<Hotels> findByName(String name) {
        try {
            return dao.findByName(name);
        } catch (Exception e) {
            throw new RepositoryException("Failed to fetch Hotels by name: " + name, e);
        }
    }

    @Override
    public List<Hotels> getAll() {
        try {
            return dao.getAll();
        } catch (Exception e) {
            throw new RepositoryException("Failed to fetch all Hotels", e);
        }
    }

    @Override
    public void insert(UUID id, String name, int stars) {
        try {
            dao.insert(id, name, stars);
        } catch (Exception e) {
            throw new RepositoryException("Failed to insert hotel: " + name, e);
        }
    }

    @Override
    public boolean update(UUID id, String name, int stars) {
        try {
            return dao.update(id, name, stars) > 0;
        } catch (Exception e) {
            throw new RepositoryException("Failed to update hotel with id: " + id, e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            dao.deleteById(id);
        } catch (Exception e) {
            throw new RepositoryException("Failed to delete hotel with id: " + id, e);
        }
    }

    @Override
    public void save(Hotels hotel) {
        try {
            dao.insert(hotel.getId(), hotel.getName(), hotel.getStars());
        } catch (Exception e) {
            throw new RepositoryException("Failed to save Hotels", e);
        }
    }
}