package com.example.ecommerceProject.service.login;

import com.example.ecommerceProject.dto.LoginDto;
import com.example.ecommerceProject.dto.ResponseDto;
import com.example.ecommerceProject.dto.TokenResponseDto;
import com.example.ecommerceProject.model.tokenStore.JwtToken;
import com.example.ecommerceProject.model.user.User;
import com.example.ecommerceProject.repository.JwtTokenRepo;
import com.example.ecommerceProject.repository.LoginService;
import com.example.ecommerceProject.repository.ResponseService;
import com.example.ecommerceProject.repository.UserRepo;
import com.example.ecommerceProject.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {


    final UserRepo userRepo;
    final AuthenticationManager authenticationManager;

    final JwtTokenRepo jwtTokenRepo;

    final PasswordEncoder passwordEncoder;
    final ResponseService responseService;

    final JwtService jwtService;

    public LoginServiceImpl(UserRepo userRepo, AuthenticationManager authenticationManager, JwtTokenRepo jwtTokenRepo, PasswordEncoder passwordEncoder, ResponseService responseService, JwtService jwtService) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.jwtTokenRepo = jwtTokenRepo;
        this.passwordEncoder = passwordEncoder;
        this.responseService = responseService;
        this.jwtService = jwtService;
    }


    @Override
    public ResponseEntity<ResponseDto> login(LoginDto loginDto) {
        log.info("Login with this email: {}", loginDto.getEmail());
        User user = userRepo.findByEmail(loginDto.getEmail());

        if (!userRepo.existsByEmail(loginDto.getEmail())) {
            return responseService.badRequest(null, "Email doesn't exist");
        }

        if (user == null || !user.getIsActive()) {
            return handleInactiveUserResponse();
        }

        if (user.getIsLocked()) {
            return handleLockedUserResponse();
        }

        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword()) && user.getInvalidAttemptCount() < 3) {
            return generateTokenResponse(user, loginDto);
        } else {
            handleFailedLogin(user);
            int attemptsLeft = 3 - user.getInvalidAttemptCount();
            if (attemptsLeft > 0) {
                return responseService.badRequest(null, "Password is incorrect. " + attemptsLeft + " more attempts left");
            } else {
                return responseService.badRequest(null, "Account is locked");
            }
        }
    }

    private ResponseEntity<ResponseDto> handleInactiveUserResponse() {
        return responseService.badRequest(null, "User account is not active");
    }

    private ResponseEntity<ResponseDto> handleLockedUserResponse() {
        return responseService.locked(null, "Account is locked");
    }

    private void handleFailedLogin(User user) {
        user.setInvalidAttemptCount(user.getInvalidAttemptCount() + 1);

        if (user.getInvalidAttemptCount() >= 3) {
            user.setIsLocked(true);
        }

        userRepo.save(user);
    }

    private ResponseEntity<ResponseDto> generateTokenResponse(User user, LoginDto loginDto) {
        user.setInvalidAttemptCount(0); // Reset the invalid attempt count
        userRepo.save(user);
        if (shouldGenerateNewToken(user)) {
            deleteExpiredToken(user);
            String jwtToken = generateJwtToken(loginDto, user);
            return responseService.success(new TokenResponseDto(jwtToken), null);
        } else {
            return responseService.success(new TokenResponseDto(user.getJwtToken().getToken()), null);
        }
    }

    private boolean shouldGenerateNewToken(User user) {
        return jwtTokenRepo.getByUserId(user.getId()) == null || !user.getJwtToken().getExpireTime().isAfter(LocalDateTime.now());
    }

    private void deleteExpiredToken(User user) {
        if (user.getJwtToken() != null) {
            jwtTokenRepo.deleteById(user.getJwtToken().getId());
        }
    }

    @Override
    public String generateJwtToken(LoginDto loginDto, User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtService.generateToken(loginDto.getEmail(), 24L);
        JwtToken loginJwtToken = new JwtToken();
        loginJwtToken.setToken(token);
        loginJwtToken.setUser(user);
        loginJwtToken.setExpireTime(LocalDateTime.now().plusMinutes(24));
        jwtTokenRepo.save(loginJwtToken);
        return token;
    }


}

