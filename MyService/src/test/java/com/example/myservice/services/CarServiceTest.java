package com.example.myservice.services;

import com.example.myservice.entities.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CarServiceTest {

    private CarService carService;

    @BeforeEach
    public void setUp() {
        carService = new CarService();
    }

    @Test
    public void testAddCar() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        carService.addCar(car);
        assertEquals(1, carService.getCars().size());
    }

    @Test
    public void testGetCars() {
        Car car1 = new Car("ABC123", "Toyota", 15000.0);
        Car car2 = new Car("XYZ789", "Honda", 18000.0);
        carService.addCar(car1);
        carService.addCar(car2);
        assertEquals(2, carService.getCars().size());
    }

    @Test
    public void testGetCarByPlateNumber() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        carService.addCar(car);
        Car result = carService.getCar("ABC123");
        assertNotNull(result);
        assertEquals("ABC123", result.getPlateNumber());
        assertEquals("Toyota", result.getBrand());
    }

    @Test
    public void testGetCarNotFound() {
        Car result = carService.getCar("NOTFOUND");
        assertNull(result);
    }

    @Test
    public void testDeleteCar() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        carService.addCar(car);
        boolean deleted = carService.deleteCar("ABC123");
        assertTrue(deleted);
        assertEquals(0, carService.getCars().size());
    }

    @Test
    public void testDeleteCarNotFound() {
        boolean deleted = carService.deleteCar("NOTFOUND");
        assertFalse(deleted);
    }

    @Test
    public void testAddMultipleCars() {
        Car car1 = new Car("ABC123", "Toyota", 15000.0);
        Car car2 = new Car("XYZ789", "Honda", 18000.0);
        Car car3 = new Car("DEF456", "Ford", 20000.0);
        
        carService.addCar(car1);
        carService.addCar(car2);
        carService.addCar(car3);
        
        assertEquals(3, carService.getCars().size());
    }
}
