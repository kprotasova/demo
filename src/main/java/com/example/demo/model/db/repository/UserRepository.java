package com.example.demo.model.db.repository;

import com.example.demo.model.db.entity.User;
import com.example.demo.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    @Query("select u from User u where u.status <> :status")
    Page<User> findAllByStatusNot(Pageable pageRequest, UserStatus status);

    @Query("select u from User u where u.status <> :status and (lower(u.firstName) like %:filter% or lower(u.lastName) like %:filter%)")
    Page<User> findAllByStatusNotFiltered(Pageable pageRequest, UserStatus status, String filter);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);
}
