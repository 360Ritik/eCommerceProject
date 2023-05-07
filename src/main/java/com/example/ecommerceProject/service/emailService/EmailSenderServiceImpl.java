package com.example.ecommerceProject.service.emailService;

import com.example.ecommerceProject.repository.EmailSenderRepo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderRepo {

    final
    JavaMailSender javaMailSender;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendSimpleEmail(String toEmail, String token, String userType) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("ritik.kumar@tothenew.com");
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Email Activation");
        mailMessage.setText("To activate your account, please verify your email on the given link with the below token:" +
                " " + "\n"
                + "http://localhost:8080/activation/" + userType + "\n\n"
                + "Token is valid for 15 minutes " + "\n" + token);
        javaMailSender.send(mailMessage);
        System.out.println("Mail send successfully!");
    }
}