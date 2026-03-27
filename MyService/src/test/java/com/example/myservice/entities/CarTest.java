package com.example.myservice.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CarTest {

    @Test
    public void testCarConstructor() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        assertEquals("ABC123", car.getPlateNumber());
        assertEquals("Toyota", car.getBrand());
        assertEquals(15000.0, car.getPrice());
    }

    @Test
    public void testSetPlateNumber() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        car.setPlateNumber("XYZ789");
        assertEquals("XYZ789", car.getPlateNumber());
    }

    @Test
    public void testSetBrand() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        car.setBrand("Honda");
        assertEquals("Honda", car.getBrand());
    }

    @Test
    public void testSetPrice() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        car.setPrice(20000.0);
        assertEquals(20000.0, car.getPrice());
    }

    @Test
    public void testToString() {
        Car car = new Car("ABC123", "Toyota", 15000.0);
        String expected = "Car{plateNumber='ABC123', brand='Toyota', price=15000.0}";
        assertEquals(expected, car.toString());
    }
}
