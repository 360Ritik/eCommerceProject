package com.example.ecommerceProject.repository;

import com.example.ecommerceProject.model.tokenStore.RegisterToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RegisterTokenRepo extends JpaRepository<RegisterToken, UUID> {


    Boolean existsRegisterTokenByUuidToken(String token);

    RegisterToken findById(String uuid);

    RegisterToken getRegisterTokenByUuidToken(String token);


    void deleteById(Long id);

    Optional<RegisterToken> findByUuidToken(String token);
}
