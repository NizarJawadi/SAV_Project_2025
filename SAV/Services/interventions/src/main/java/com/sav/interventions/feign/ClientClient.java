package com.sav.interventions.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client-service" , url = "http://localhost:9090")
public interface ClientClient {

    @GetMapping("/client/{id}")
    ResponseEntity<Object> getClientById(@PathVariable Long id);
}