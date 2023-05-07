package com.example.ecommerceProject.repository;

import com.example.ecommerceProject.enums.Authority;
import com.example.ecommerceProject.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByAuthority(Authority customer);


    Role getByAuthority(Authority admin);

}


