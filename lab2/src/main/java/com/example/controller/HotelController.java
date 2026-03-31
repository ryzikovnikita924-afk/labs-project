package com.example.controller;

import com.example.service.HotelService;
import com.example.dto.HotelInitialization;
import com.example.dto.Hotels;
import com.example.dto.HotelsList;
import com.example.dto.UniversalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<UniversalResponse<HotelsList>> getAllHotels() {
        log.info("GET /api/hotels - получение всех отелей");
        return ResponseEntity.ok(hotelService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversalResponse<Hotels>> getHotelById(@PathVariable UUID id) {
        log.info("GET /api/hotels/{} - получение отеля по ID", id);
        return ResponseEntity.ok(hotelService.getById(id));
    }

    @PostMapping
    public ResponseEntity<UniversalResponse<Hotels>> createHotel(@RequestBody HotelInitialization request) {
        log.info("POST /api/hotels - создание нового отеля");
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UniversalResponse<Hotels>> updateHotel(
            @PathVariable UUID id,
            @RequestBody HotelInitialization request) {
        log.info("PUT /api/hotels/{} - обновление отеля", id);
        return ResponseEntity.ok(hotelService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UniversalResponse<Void>> deleteHotel(@PathVariable UUID id) {
        log.info("DELETE /api/hotels/{} - удаление отеля", id);
        return ResponseEntity.ok(hotelService.deleteById(id));
    }
}