package com.sav.factureservice.feign;
import com.sav.factureservice.model.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "client-service" , url = "http://localhost:9090")
public interface ClientFeignClient {
    @GetMapping("/client/{id}")
    Client getClientById(@PathVariable("id") Long id);
}
