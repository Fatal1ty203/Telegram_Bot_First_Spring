package com.example.demo.springtelegrammbot.repository;


import com.example.demo.springtelegrammbot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
