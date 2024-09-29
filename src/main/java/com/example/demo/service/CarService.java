package com.example.demo.service;


import com.example.demo.model.db.entity.Car;
import com.example.demo.model.db.entity.User;
import com.example.demo.model.db.repository.CarRepository;
import com.example.demo.model.dto.request.CarInfoRequest;
import com.example.demo.model.dto.request.CarToUserRequest;
import com.example.demo.model.dto.response.CarInfoResponse;
import com.example.demo.model.enums.CarStatus;
import com.example.demo.model.enums.UserStatus;
import com.example.demo.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {
    private final UserService userService;
    public final ObjectMapper mapper;
    private final CarRepository carRepository;

    public CarInfoResponse createCar(CarInfoRequest request) {
        Car car = mapper.convertValue(request, Car.class);
        car.setStatus(CarStatus.CREATED);
        return mapper.convertValue(carRepository.save(car), CarInfoResponse.class);
    }


    public CarInfoResponse getCar(Long id) {
        return mapper.convertValue(carRepository.findById(id), CarInfoResponse.class);
    }

    private Car getCarById(Long id) {
        return carRepository.findById(id).orElse(new Car());
    }

    public CarInfoResponse updateCar(Long id, CarInfoRequest request) {
        return null;
    }

    public void delete(Long id) {
        Car car = getCarById(id);
        car.setStatus(CarStatus.DELETED);
        carRepository.save(car);
    }

    public Page<CarInfoResponse> getAllCars(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {

        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);

        Page<Car> all;
        if (filter == null) {
            all = carRepository.findAllByStatusNot(pageRequest, CarStatus.DELETED);
        } else {
            all = carRepository.findAllByStatusNotFiltered(pageRequest, CarStatus.DELETED, filter.toLowerCase());
        }

            List<CarInfoResponse> content = all.getContent().stream()
                    .map(car -> mapper.convertValue(car, CarInfoResponse.class))
                    .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, all.getTotalElements());
    }

    public void addCarToUser(CarToUserRequest request) {
        Car car = carRepository.findById(request.getCarId()).orElse(null);

        if (car == null) {
            return;
        }

        User userFromDB = userService.getUserFromDB(request.getUserId());

        if (userFromDB == null) {
            return;
        }

        userFromDB.getCars().add(car);

        userService.updateUserData(userFromDB);

        car.setUser(userFromDB);
        carRepository.save(car);
    }
}
