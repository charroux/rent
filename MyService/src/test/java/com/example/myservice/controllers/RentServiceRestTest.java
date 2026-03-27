package com.example.myservice.controllers;

import com.example.myservice.entities.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class RentServiceRestTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @org.junit.jupiter.api.BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddCar() throws Exception {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        ObjectMapper objectMapper = new ObjectMapper();
        
        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCars() throws Exception {
        // Ajouter une voiture d'abord
        Car car1 = new Car("ABC123", "Toyota", 15000.0);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car1)));
        
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCar() throws Exception {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)));

        mockMvc.perform(delete("/cars/ABC123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCarByPlateNumber() throws Exception {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Ajouter une voiture d'abord
        mockMvc.perform(post("/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(car)));
        
        mockMvc.perform(get("/cars/ABC123"))
                .andExpect(status().isOk());
    }
}
