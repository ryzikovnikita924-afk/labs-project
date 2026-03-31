package com.example.service;

import com.example.dto.HotelInitialization;
import com.example.dto.Hotels;
import com.example.dto.HotelsList;
import com.example.dto.UniversalResponse;
import com.example.exceptions.EntityNotFound;
import com.example.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelsRepository;

    public UniversalResponse<Hotels> create(HotelInitialization request) {
        log.info("Creating new hotel: {}", request.getName());
        UUID id = save(request.getName(), request.getStars());
        return getById(id);
    }

    public UUID save(String name, int stars) {
        log.info("Creating new hotel: {}", name);

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название отеля не может быть пустым");
        }
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Количество звезд должно быть от 1 до 5");
        }

        UUID id = UUID.randomUUID();
        Hotels hotel = new Hotels(id, name, stars);
        hotelsRepository.save(hotel);

        return id;
    }

    public UniversalResponse<Hotels> getById(UUID id) {
        log.debug("Getting hotel by id: {}", id);
        Hotels hotel = hotelsRepository.getById(id)
                .orElseThrow(() -> new EntityNotFound("Отель с id " + id + " не найден"));
        return new UniversalResponse<>(hotel);
    }

    public UniversalResponse<Hotels> getByName(String name) {
        log.debug("Getting hotel by name: {}", name);

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название отеля не может быть пустым");
        }

        Hotels hotel = hotelsRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFound("Отель с названием '" + name + "' не найден"));
        return new UniversalResponse<>(hotel);
    }

    public UniversalResponse<HotelsList> getAll() {
        log.debug("Getting all hotels");
        List<Hotels> hotels = hotelsRepository.getAll();
        if (hotels.isEmpty()) {
            throw new EntityNotFound("Отели не найдены");
        }
        return new UniversalResponse<>(new HotelsList(hotels));
    }

    public UniversalResponse<Hotels> update(UUID id, HotelInitialization request) {
        log.info("Updating hotel with id: {}", id);

        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Название отеля не может быть пустым");
        }
        if (request.getStars() < 1 || request.getStars() > 5) {
            throw new IllegalArgumentException("Количество звезд должно быть от 1 до 5");
        }

        Hotels existingHotel = hotelsRepository.getById(id)
                .orElseThrow(() -> new EntityNotFound("Отель с id " + id + " не найден"));

        boolean updated = hotelsRepository.update(id, request.getName(), request.getStars());

        if (!updated) {
            throw new RuntimeException("Не удалось обновить отель с id: " + id);
        }

        Hotels updatedHotel = hotelsRepository.getById(id)
                .orElseThrow(() -> new RuntimeException("Не удалось найти обновленный отель"));

        return new UniversalResponse<>(updatedHotel);
    }

    public UniversalResponse<Void> deleteById(UUID id) {
        log.info("Deleting hotel with id: {}", id);

        Hotels hotel = hotelsRepository.getById(id)
                .orElseThrow(() -> new EntityNotFound("Отель с id " + id + " не найден"));

        hotelsRepository.deleteById(id);

        return new UniversalResponse<>(null);
    }
}