package com.symplifica.backend.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    
    @Value("classpath:templates/newsletter.html")
    private Resource htmlTemplate;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendNewsSummaryEmail(String toEmail, String summaryContent) {
        try {
            String htmlContent;
            if (htmlTemplate.exists()) {
                htmlContent = StreamUtils.copyToString(htmlTemplate.getInputStream(), StandardCharsets.UTF_8);
                htmlContent = htmlContent.replace("{{SUMMARY_CONTENT}}", summaryContent.replace("\n", "<br/>"));
            } else {
                htmlContent = "<html><body><h1>Resumen de Noticias</h1><p>" + summaryContent.replace("\n", "<br/>") + "</p></body></html>";
            }

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom("newsletter@symplifica.com.co");
            helper.setTo(toEmail);
            helper.setSubject("Tu Resumen de Noticias Principal - Symplifica");
            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error enviando email: " + e.getMessage());
        }
    }
}
