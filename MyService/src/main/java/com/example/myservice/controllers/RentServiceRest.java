package com.example.myservice.controllers;

import com.example.myservice.entities.Car;
import com.example.myservice.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class RentServiceRest {

    @Autowired
    private CarService carService;

    @GetMapping("/")
    public String sayHello(){
        return "Hello shxru";
    }

    @PostMapping("/cars")
    public void addCar(@RequestBody Car car){
        carService.addCar(car);
    }

    @GetMapping("/cars")
    public List<Car> getCars(){
        return carService.getCars();
    }

    @GetMapping("/cars/{plateNumber}")
    public Car getCar(@PathVariable String plateNumber){
        return carService.getCar(plateNumber);
    }

}
