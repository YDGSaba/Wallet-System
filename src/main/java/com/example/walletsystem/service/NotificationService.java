package com.example.walletsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private EmailSenderService emailSenderService;

    public void sendTransactionNotification(String email, String transactionDetails) {
        String subject = "Transaction Successful";
        String message = "Your transaction was completed successfully. Details: " + transactionDetails;
        emailSenderService.sendEmail(email, subject, message);
    }
    public void sendLowBalanceNotification(String email) {
        String subject = "Low Account Balance";
        String message = "Your account balance is running low. Please deposit funds to avoid issues.";
        emailSenderService.sendEmail(email, subject, message);
    }
    public void sendTransactionLimitReachedNotification(String email) {
        String subject = "Daily Transaction Limit Reached";
        String message = "You have reached your daily transaction limit. No further transactions can be processed today.";
        emailSenderService.sendEmail(email, subject, message);
    }
}
