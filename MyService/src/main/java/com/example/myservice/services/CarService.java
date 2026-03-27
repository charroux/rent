package com.example.myservice.services;

import com.example.myservice.entities.Car;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    private List<Car> cars = new ArrayList<>();

    public void addCar(Car car) {
        cars.add(car);
    }

    public Car getCar(String plateNumber) {
        return cars.stream()
                .filter(car -> car.getPlateNumber().equals(plateNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Car> getCars() {
        return new ArrayList<>(cars);
    }

    public boolean deleteCar(String plateNumber) {
        return cars.removeIf(car -> car.getPlateNumber().equals(plateNumber));
    }
}
