package com.example.ecommerceProject.bootstrap;


import com.example.ecommerceProject.enums.Authority;
import com.example.ecommerceProject.model.user.Address;
import com.example.ecommerceProject.model.user.Role;
import com.example.ecommerceProject.model.user.User;
import com.example.ecommerceProject.repository.RoleRepo;
import com.example.ecommerceProject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BootstrapApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // createRoleIfNotFound(Authority.ADMIN);
        createRoleIfNotFound(Authority.CUSTOMER);
        createRoleIfNotFound(Authority.SELLER);
        createAdminProfile();
    }

    private void createRoleIfNotFound(Authority name) {
        Role role = roleRepo.findByAuthority(name);
        if (role == null) {
            role = new Role();
            role.setAuthority(name);
            roleRepo.save(role);
        }
    }

    public void createAdminProfile() {

        User user1 = userRepo.findByFirstName("ADMIN");

        if (user1 == null) {
            User user = new User();
            user.setFirstName("ADMIN");
            user.setEmail("Admin123@gmail.com");
            user.setPassword("Passadmin@123");
            user.setCreatedBy("admin");
            user.setIsActive(true);

            Address address1 = new Address();
            address1.setAddressLine("line");
            address1.setCity("rohtak");
            address1.setCountry("india");
            address1.setLabel("label1");
            address1.setZipCode(12334);
            address1.setState("delhi");
            address1.setUser(user);
            List<Address> addressList = List.of(address1);


            Role adminRole = new Role();
            adminRole.setAuthority(Authority.ADMIN);
            List<Role> role = new ArrayList<>();
            role.add(adminRole);


            user.setRoles(role);
            user.setAddresses(addressList);
            userRepo.save(user);


        }


    }
}