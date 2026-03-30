import com.example.HotelApplication;
import com.example.Service.HotelService;
import com.example.dto.HotelInitialization;
import com.example.dto.Hotels;
import com.example.dto.HotelsList;
import com.example.dto.UniversalResponse;
import com.example.repository.HotelController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HotelController.class)
@ContextConfiguration(classes = HotelApplication.class)  // Указываем класс конфигурации
class HotelControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID hotelId;
    private Hotels hotel;

    @BeforeEach
    void setUp() {
        hotelId = UUID.randomUUID();
        hotel = new Hotels(hotelId, "Hilton", 5);
    }

    @Test
    void testGetAllHotels() throws Exception {
        UniversalResponse<HotelsList> response = new UniversalResponse<>(
                new HotelsList(List.of(hotel))
        );

        when(hotelService.getAll()).thenReturn(response);

        mockMvc.perform(get("/api/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.hotels[0].name").value("Hilton"))
                .andExpect(jsonPath("$.data.hotels[0].stars").value(5));
    }

    @Test
    void testGetHotelById() throws Exception {
        UniversalResponse<Hotels> response = new UniversalResponse<>(hotel);

        when(hotelService.getById(hotelId)).thenReturn(response);

        mockMvc.perform(get("/api/hotels/{id}", hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Hilton"))
                .andExpect(jsonPath("$.data.stars").value(5));
    }

    @Test
    void testCreateHotel() throws Exception {
        HotelInitialization request = new HotelInitialization("Hilton", 5);
        UniversalResponse<Hotels> response = new UniversalResponse<>(hotel);

        when(hotelService.create(any(HotelInitialization.class))).thenReturn(response);

        mockMvc.perform(post("/api/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Hilton"))
                .andExpect(jsonPath("$.data.stars").value(5));
    }

    @Test
    void testUpdateHotel() throws Exception {
        HotelInitialization request = new HotelInitialization("HiltonUpdated", 4);
        Hotels updatedHotel = new Hotels(hotelId, "HiltonUpdated", 4);
        UniversalResponse<Hotels> response = new UniversalResponse<>(updatedHotel);

        when(hotelService.update(eq(hotelId), any(HotelInitialization.class))).thenReturn(response);

        mockMvc.perform(put("/api/hotels/{id}", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("HiltonUpdated"))
                .andExpect(jsonPath("$.data.stars").value(4));
    }

    @Test
    void testDeleteHotel() throws Exception {
        UniversalResponse<Void> response = new UniversalResponse<>(null);

        when(hotelService.deleteById(hotelId)).thenReturn(response);

        mockMvc.perform(delete("/api/hotels/{id}", hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}