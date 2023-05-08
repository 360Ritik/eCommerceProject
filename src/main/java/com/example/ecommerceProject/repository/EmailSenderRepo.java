package com.example.ecommerceProject.repository;


public interface EmailSenderRepo {
    void sendSimpleEmail(String toEmail, String subject, String body);
}
