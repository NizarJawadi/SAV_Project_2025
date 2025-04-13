package com.sav.historiqueachat.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:9090/client")
public interface UserServiceClient {
    @GetMapping("/{id}")
    Object getClientById(@PathVariable("id") Long id);
}