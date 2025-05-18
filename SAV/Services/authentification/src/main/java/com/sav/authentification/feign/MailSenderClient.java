package com.sav.authentification.feign;

import com.sav.authentification.utils.MailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mail-sender", url = "http://localhost:3333/api/mail") // adapte le port si nécessaire
public interface MailSenderClient {

    @PostMapping("/send")
    ResponseEntity<String> sendMail(@RequestBody MailRequest mailRequest);
}
