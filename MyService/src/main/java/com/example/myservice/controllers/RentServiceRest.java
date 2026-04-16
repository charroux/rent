package com.example.myservice.controllers;

import com.example.myservice.entities.Car;
import com.example.myservice.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class RentServiceRest {

    private final CarService carService;

    public RentServiceRest(CarService carService) {
        this.carService = carService;
    }

    @PostMapping()
    public void addCar(@RequestBody Car car){
        carService.addCar(car);
    }

    @GetMapping()
    public List<Car> getCars(){
        return carService.getCars();
    }

    @GetMapping("/{plateNumber}")
    public Car getCar(@PathVariable String plateNumber){
        return carService.getCar(plateNumber);
    }

}
