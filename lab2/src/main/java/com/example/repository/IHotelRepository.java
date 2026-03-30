package com.example.repository;

import com.example.dto.Hotels;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IHotelRepository {
    Optional<Hotels> getById(UUID id);
    Optional<Hotels> findByName(String name);
    List<Hotels> getAll();
    void insert(UUID id, String name, int stars);
    boolean update(UUID id, String name, int stars);
    void deleteById(UUID id);
    void save(Hotels hotel);
}