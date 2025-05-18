package com.sav.mailsender.controller;

import com.sav.mailsender.model.EmailRequestDto;
import com.sav.mailsender.model.MailRequest;
import com.sav.mailsender.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody MailRequest mailRequest) {
        mailService.sendEmail(mailRequest.getTo(), mailRequest.getSubject(), mailRequest.getBody());
        return ResponseEntity.ok("Email sent successfully!");
    }

    @PostMapping("/send-mail-with-attachment")
    public ResponseEntity<?> sendMailWithAttachment(@RequestBody EmailRequestDto emailRequest) {
        mailService.sendEmailWithAttachment(
                emailRequest.getTo(),
                emailRequest.getSubject(),
                emailRequest.getText(),
                Base64.getDecoder().decode(emailRequest.getAttachment()),
                emailRequest.getFilename()
        );
        return ResponseEntity.ok("Email sent successfully");
    }

}
