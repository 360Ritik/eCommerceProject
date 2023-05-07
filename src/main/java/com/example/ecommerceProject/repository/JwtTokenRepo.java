package com.example.ecommerceProject.repository;

import com.example.ecommerceProject.model.tokenStore.JwtToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtTokenRepo extends CrudRepository<JwtToken, Long> {

    JwtToken findByToken(String token);

    void deleteById(Long id);

    boolean existsAllByUserId(Long id);

    JwtToken getByUserId(Long id);
}
