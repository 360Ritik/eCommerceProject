package com.example.ecommerceProject.repository;


import com.example.ecommerceProject.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<User, Long> {


    boolean existsByEmail(String email);

    User findByEmail(String username);


    User findByFirstName(String admin);

    User getByEmail(String customerEmail);


}
