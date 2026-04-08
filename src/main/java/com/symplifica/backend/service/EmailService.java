package com.symplifica.backend.service;

public interface EmailService {
    void sendNewsSummaryEmail(String toEmail, String summaryContent);
}
