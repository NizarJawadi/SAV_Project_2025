package com.sav.interventions.feign;

import com.sav.interventions.utils.EmailRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mail-service", url = "http://localhost:3333/api/mail") // adapte le port
public interface MailFeignClient {

    @PostMapping("/send-mail-with-attachment")
    ResponseEntity<String> sendMailWithAttachment(@RequestBody EmailRequestDto emailRequest);
}
