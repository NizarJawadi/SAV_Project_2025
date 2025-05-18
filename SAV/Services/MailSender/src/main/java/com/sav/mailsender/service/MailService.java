package com.sav.mailsender.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("assistant.sav.techflow@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }


    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachmentBytes, String filename) {
        try {
            MimeMessage message = mailSender.createMimeMessage(); // corrigé ici
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom("assistant.sav.techflow@gmail.com");
            ByteArrayDataSource dataSource = new ByteArrayDataSource(attachmentBytes, "application/pdf");
            helper.addAttachment(filename, dataSource);

            mailSender.send(message); // corrigé ici aussi
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


}
