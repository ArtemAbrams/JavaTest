package org.example.javatest.repository;

import org.example.javatest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    List<User> findByBirthDateBetween(LocalDate start, LocalDate end);
}
