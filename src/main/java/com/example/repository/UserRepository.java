package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);

    UserEntity findByLastName(String lastName);

}
