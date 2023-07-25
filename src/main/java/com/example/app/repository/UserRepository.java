package com.example.app.repository;

import com.example.app.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByEmail(String email);
    Boolean existsByEmail(String email);
}
