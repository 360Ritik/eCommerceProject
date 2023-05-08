package com.example.ecommerceProject.service.password;

import com.example.ecommerceProject.dto.PasswordChangeDto;
import com.example.ecommerceProject.dto.ResponseDto;
import com.example.ecommerceProject.model.tokenStore.RegisterToken;
import com.example.ecommerceProject.model.user.User;
import com.example.ecommerceProject.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class PasswordServiceImple implements PasswordService {


    final UserRepo userRepo;

    final RegisterTokenRepo registerTokenRepo;

    final EmailSenderRepo emailSenderService;

    final PasswordEncoder passwordEncoder;

    final ResponseService responseService;

    public PasswordServiceImple(UserRepo userRepo, RegisterTokenRepo registerTokenRepo, EmailSenderRepo emailSenderService, PasswordEncoder passwordEncoder, ResponseService responseService) {
        this.userRepo = userRepo;
        this.registerTokenRepo = registerTokenRepo;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
        this.responseService = responseService;
    }

    @Override
    public ResponseEntity<ResponseDto> resetLink(String email) {
        log.info("Forget Password API is called of this email: {}", email);
        if (!userRepo.existsByEmail(email)) {
            return responseService.badRequest(null, "Email doesn't exists");
        }
        User user = userRepo.findByEmail(email);
        if (!user.getIsActive() || user.getIsLocked()) {
            return responseService.badRequest(null, "Account is not active or locked");
        }

        if (user.getRegisterToken() != null && registerTokenRepo.existsRegisterTokenByUuidToken(user.getRegisterToken().getUuidToken())) {
            registerTokenRepo.deleteById(user.getRegisterToken().getId());
        }

        System.out.println("resentLink");
        String token = UUID.randomUUID().toString();
        RegisterToken registerToken = new RegisterToken();
        registerToken.setValid(LocalDateTime.now().plusMinutes(15));
        registerToken.setUuidToken(token);
        registerToken.setUser(user);
        registerTokenRepo.save(registerToken);

        String body = "To change your password, please click on given link " +
                "http://localhost:8080/auth/reset/password?token=" + token;
        emailSenderService.sendSimpleEmail(email, body, "Reset Password");
        return responseService.success(null, "Email is successfully sent for reset password");


    }

    @Override
    public ResponseEntity<ResponseDto> resetPassword(String token, PasswordChangeDto forgetPasswordDto) {
        log.info("Reset Password API is called with this token: {}", token);
        if (!registerTokenRepo.existsRegisterTokenByUuidToken(token)) {
            return responseService.badRequest(null, "Token is incorrect");
        }
        RegisterToken registerToken = registerTokenRepo.getRegisterTokenByUuidToken(token);
        if (registerToken.getValid().isBefore(LocalDateTime.now())) {
            registerTokenRepo.deleteById(registerToken.getId());
            return responseService.badRequest(null, "Token is expired");
        }

        User user = userRepo.findByEmail(registerToken.getUser().getEmail());
        user.setPassword(passwordEncoder.encode(forgetPasswordDto.getPassword()));
        user.setPasswordUpdateDate(LocalDateTime.now());
        userRepo.save(user);
        registerTokenRepo.deleteById(registerToken.getId());
        return responseService.success(null, "Password is successfully changed!");



    }
}
