package com.example.ecommerceProject.security;


import com.example.ecommerceProject.model.user.Role;
import com.example.ecommerceProject.model.user.User;
import com.example.ecommerceProject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class
UserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (repository.existsByEmail(email)) {
            User userInfo = repository.findByEmail(email);
            return new org.springframework.security.core.userdetails.User(userInfo.getEmail(),
                    userInfo.getPassword(), UserInfoUserDetail(userInfo.getRoles()));
        } else {
            throw new UsernameNotFoundException("user not found " + email);
        }
    }


    private Collection<GrantedAuthority> UserInfoUserDetail(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority().toString())).collect(Collectors.toList());
    }
}