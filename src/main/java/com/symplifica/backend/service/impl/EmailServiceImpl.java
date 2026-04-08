package com.symplifica.backend.service.impl;

import com.symplifica.backend.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

import com.symplifica.backend.utils.Constants;
import com.symplifica.backend.utils.Messages;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    
    @Value("classpath:templates/newsletter.html")
    private Resource htmlTemplate;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendNewsSummaryEmail(String toEmail, String summaryContent) {
        try {
            String htmlContent;
            if (htmlTemplate.exists()) {
                htmlContent = StreamUtils.copyToString(htmlTemplate.getInputStream(), StandardCharsets.UTF_8);
                htmlContent = htmlContent.replace(Constants.KEY_SUMMARY_CONTENT, summaryContent.replace("\n", "<br/>"));
            } else {
                htmlContent = Messages.MSG_EMAIL_FALLBACK_HTML(summaryContent.replace("\n", "<br/>"));
            }

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(Constants.SENDER_EMAIL);
            helper.setTo(toEmail);
            helper.setSubject(Messages.MSG_EMAIL_SUBJECT);
            helper.setText(htmlContent, true);

            emailSender.send(message);
        } catch (Exception e) {
            System.err.println(Messages.MSG_EMAIL_SEND_ERROR + e.getMessage());
        }
    }
}
