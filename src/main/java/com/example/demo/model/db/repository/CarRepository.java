package com.example.demo.model.db.repository;

import com.example.demo.model.db.entity.Car;
import com.example.demo.model.enums.CarStatus;
import com.example.demo.model.enums.Color;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByModelAndStatus(String model, CarStatus status);

    List<Car> findAllByColor(Color color);

    @Query(nativeQuery = true, value ="select * from cars where id > 2 limit 1")
    Car getCar();

//    @Query(nativeQuery = true, value ="select * from cars where is_new = :isNew limit 1")
//    Car getSomeCar(@Param("isNew") boolean isNew);

    @Query("select c from Car c where c.status <> 'DELETED'")
    List<Car> findAllDeletedCars();

    @Query("select c from Car c where c.status <> :status")
    Page<Car> findAllByStatusNot(Pageable request, CarStatus status);

    @Query("select c from Car c where c.status <> :status and lower(c.model) like %:filter%")
    Page<Car> findAllByStatusNotFiltered(Pageable pageRequest, CarStatus status, String filter);
}


