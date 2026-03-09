package repository;

import dto.hotels;
import exceptions.RepositoryException;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.jdbi.v3.sqlobject.customizer.Bind;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HotelsRepository {
    private final HotelsDao dao;

    public HotelsRepository(DataSource dataSource) {
        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
        this.dao = jdbi.onDemand(HotelsDao.class);
    }

    public Optional<hotels> getById(UUID id) {
        try {
            return dao.getById(id);
        } catch (Exception e) {
            throw new RepositoryException("Failed to fetch hotels by id", e);
        }
    }

    public List<hotels> getAll() {
        try {
            return dao.getAll();
        } catch (Exception e) {
            throw new RepositoryException("Failed to fetch all hotels", e);
        }
    }

    public void insert(UUID id, String name, int stars) {
        try {
            System.out.println("=== INSERT DEBUG ===");
            System.out.println("ID: " + id);
            System.out.println("Name: " + name);
            System.out.println("Stars: " + stars);
            System.out.println("ID class: " + id.getClass().getName());

            dao.insert(id, name, stars);

            System.out.println("INSERT успешен для: " + name);
        } catch (Exception e) {
            System.err.println("INSERT ERROR для: " + name);
            System.err.println("Exception type: " + e.getClass().getName());
            System.err.println("Exception message: " + e.getMessage());
            e.printStackTrace();
            throw new RepositoryException("Failed to insert hotel: " + name, e);
        }
    }

    public boolean update(UUID id, String name, int stars) {
        try {
            return dao.update(id, name, stars) > 0;
        } catch (Exception e) {
            throw new RepositoryException("Failed to update hotel with id: " + id, e);
        }
    }

    public void deleteById(UUID id) {
        try {
            dao.deleteById(id);
        } catch (Exception e) {
            throw new RepositoryException("Failed to delete hotel with id: " + id, e);
        }
    }

    public void save(hotels hotel) {
        try {
            dao.insert(hotel.getId(), hotel.getName(), hotel.getStars());
        } catch (Exception e) {
            throw new RepositoryException("Failed to save hotels", e);
        }
    }
}


