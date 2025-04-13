package com.sav.springchatbot.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "guide-service", url = "http://localhost:9999")
public interface GuideFeignClient {


}