package com.example.service;

import com.example.Main;
import com.example.dto.HotelInitialization;
import com.example.dto.Hotels;
import com.example.dto.HotelsList;
import com.example.dto.UniversalResponse;
import com.example.exceptions.EntityNotFound;
import com.example.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Main.class)
class HotelServiceTest {

    @MockBean
    private HotelRepository hotelsRepository;

    @Autowired
    private HotelService hotelService;

    private UUID testId;
    private Hotels testHotel;
    private HotelInitialization testRequest;
    private final String testName = "Test Hotel";
    private final int testStars = 4;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testHotel = new Hotels(testId, testName, testStars);
        testRequest = new HotelInitialization(testName, testStars);
    }

    @Test
    void save_ValidData_ReturnsId() {
        doAnswer(invocation -> {
            Hotels hotel = invocation.getArgument(0);
            assertNotNull(hotel.getId());
            assertEquals(testName, hotel.getName());
            assertEquals(testStars, hotel.getStars());
            return null;
        }).when(hotelsRepository).save(any(Hotels.class));

        UUID result = hotelService.save(testName, testStars);

        assertNotNull(result);
        verify(hotelsRepository, times(1)).save(any(Hotels.class));
    }

    @Test
    void save_NullName_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> hotelService.save(null, testStars));

        assertEquals("Название отеля не может быть пустым", exception.getMessage());
        verify(hotelsRepository, never()).save(any(Hotels.class));
    }

    @Test
    void save_EmptyName_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> hotelService.save("", testStars));

        assertEquals("Название отеля не может быть пустым", exception.getMessage());
        verify(hotelsRepository, never()).save(any(Hotels.class));
    }

    @Test
    void save_StarsLessThanOne_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> hotelService.save(testName, 0));

        assertEquals("Количество звезд должно быть от 1 до 5", exception.getMessage());
        verify(hotelsRepository, never()).save(any(Hotels.class));
    }

    @Test
    void save_StarsGreaterThanFive_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> hotelService.save(testName, 6));

        assertEquals("Количество звезд должно быть от 1 до 5", exception.getMessage());
        verify(hotelsRepository, never()).save(any(Hotels.class));
    }

    @Test
    void getById_ExistingId_ReturnsHotel() {
        when(hotelsRepository.getById(testId)).thenReturn(Optional.of(testHotel));

        UniversalResponse<Hotels> response = hotelService.getById(testId);

        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals("SUCCESS", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(testId, response.getData().getId());
        assertEquals(testName, response.getData().getName());
        assertEquals(testStars, response.getData().getStars());
        verify(hotelsRepository, times(1)).getById(testId);
    }

    @Test
    void getById_NonExistingId_ThrowsEntityNotFound() {
        when(hotelsRepository.getById(testId)).thenReturn(Optional.empty());

        EntityNotFound exception = assertThrows(EntityNotFound.class,
                () -> hotelService.getById(testId));

        assertEquals("Отель с id " + testId + " не найден", exception.getMessage());
        verify(hotelsRepository, times(1)).getById(testId);
    }

    @Test
    void getByName_ExistingName_ReturnsHotel() {
        when(hotelsRepository.findByName(testName)).thenReturn(Optional.of(testHotel));

        UniversalResponse<Hotels> response = hotelService.getByName(testName);

        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals("SUCCESS", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(testId, response.getData().getId());
        assertEquals(testName, response.getData().getName());
        assertEquals(testStars, response.getData().getStars());
        verify(hotelsRepository, times(1)).findByName(testName);
    }

    @Test
    void getByName_NonExistingName_ThrowsEntityNotFound() {
        when(hotelsRepository.findByName(testName)).thenReturn(Optional.empty());

        EntityNotFound exception = assertThrows(EntityNotFound.class,
                () -> hotelService.getByName(testName));

        assertEquals("Отель с названием '" + testName + "' не найден", exception.getMessage());
        verify(hotelsRepository, times(1)).findByName(testName);
    }

    @Test
    void getByName_NullName_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> hotelService.getByName(null));

        assertEquals("Название отеля не может быть пустым", exception.getMessage());
        verify(hotelsRepository, never()).findByName(anyString());
    }

    @Test
    void getAll_ExistingHotels_ReturnsHotelsList() {
        Hotels hotel2 = new Hotels(UUID.randomUUID(), "Hotel 2", 3);
        List<Hotels> hotels = Arrays.asList(testHotel, hotel2);
        when(hotelsRepository.getAll()).thenReturn(hotels);

        UniversalResponse<HotelsList> response = hotelService.getAll();

        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals("SUCCESS", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(2, response.getData().getHotels().size());

        verify(hotelsRepository, times(1)).getAll();
    }

    @Test
    void getAll_NoHotels_ThrowsEntityNotFound() {
        when(hotelsRepository.getAll()).thenReturn(Collections.emptyList());

        EntityNotFound exception = assertThrows(EntityNotFound.class,
                () -> hotelService.getAll());

        assertEquals("Отели не найдены", exception.getMessage());
        verify(hotelsRepository, times(1)).getAll();
    }

    @Test
    void create_ValidRequest_ReturnsCreatedHotel() {
        doNothing().when(hotelsRepository).save(any(Hotels.class));
        when(hotelsRepository.getById(any(UUID.class))).thenReturn(Optional.of(testHotel));

        UniversalResponse<Hotels> response = hotelService.create(testRequest);

        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals("SUCCESS", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(testName, response.getData().getName());
        assertEquals(testStars, response.getData().getStars());

        verify(hotelsRepository, times(1)).save(any(Hotels.class));
        verify(hotelsRepository, times(1)).getById(any(UUID.class));
    }

    @Test
    void update_ValidRequest_ReturnsUpdatedHotel() {
        HotelInitialization updateRequest = new HotelInitialization("Updated Hotel", 5);
        Hotels updatedHotel = new Hotels(testId, "Updated Hotel", 5);

        when(hotelsRepository.getById(testId))
                .thenReturn(Optional.of(testHotel))
                .thenReturn(Optional.of(updatedHotel));
        when(hotelsRepository.update(eq(testId), eq("Updated Hotel"), eq(5))).thenReturn(true);

        UniversalResponse<Hotels> response = hotelService.update(testId, updateRequest);

        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals("SUCCESS", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("Updated Hotel", response.getData().getName());
        assertEquals(5, response.getData().getStars());

        verify(hotelsRepository, times(2)).getById(testId);
        verify(hotelsRepository, times(1)).update(eq(testId), eq("Updated Hotel"), eq(5));
    }

    @Test
    void update_StarsGreaterThanFive_ThrowsIllegalArgumentException() {
        HotelInitialization invalidRequest = new HotelInitialization(testName, 6);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> hotelService.update(testId, invalidRequest));

        assertEquals("Количество звезд должно быть от 1 до 5", exception.getMessage());
        verify(hotelsRepository, never()).getById(any());
        verify(hotelsRepository, never()).update(any(), any(), anyInt());
    }

    @Test
    void update_NonExistingHotel_ThrowsEntityNotFound() {
        when(hotelsRepository.getById(testId)).thenReturn(Optional.empty());

        EntityNotFound exception = assertThrows(EntityNotFound.class,
                () -> hotelService.update(testId, testRequest));

        assertEquals("Отель с id " + testId + " не найден", exception.getMessage());
        verify(hotelsRepository, never()).update(any(), any(), anyInt());
    }

    @Test
    void deleteById_ExistingHotel_DeletesHotel() {
        when(hotelsRepository.getById(testId)).thenReturn(Optional.of(testHotel));
        doNothing().when(hotelsRepository).deleteById(testId);

        UniversalResponse<Void> response = hotelService.deleteById(testId);

        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals("SUCCESS", response.getMessage());
        assertNull(response.getData());

        verify(hotelsRepository, times(1)).getById(testId);
        verify(hotelsRepository, times(1)).deleteById(testId);
    }

    @Test
    void deleteById_NonExistingHotel_ThrowsEntityNotFound() {
        when(hotelsRepository.getById(testId)).thenReturn(Optional.empty());

        EntityNotFound exception = assertThrows(EntityNotFound.class,
                () -> hotelService.deleteById(testId));

        assertEquals("Отель с id " + testId + " не найден", exception.getMessage());
        verify(hotelsRepository, never()).deleteById(testId);
    }
}