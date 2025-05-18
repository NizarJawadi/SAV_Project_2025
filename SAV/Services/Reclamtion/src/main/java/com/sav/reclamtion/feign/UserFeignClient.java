package com.sav.reclamtion.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "authentification-service", url = "http://localhost:9090/User")

public interface UserFeignClient {
    @GetMapping("/client/{id}/sip")
    String getUserSipNumber(@PathVariable Long id);

}
