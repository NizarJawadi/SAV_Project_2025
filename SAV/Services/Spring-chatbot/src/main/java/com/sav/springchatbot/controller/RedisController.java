package com.sav.springchatbot.controller;

import com.sav.springchatbot.services.RedisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
@CrossOrigin(origins = "http://localhost:4200") // Autoriser les requêtes de localhost:4200
public class RedisController {

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/save")
    public String saveData(@RequestParam String key, @RequestParam String value) {
        redisService.saveData(key, value);
        return "Donnée enregistrée !";
    }

    @GetMapping("/get")
    public String getData(@RequestParam String key) {
        return redisService.getData(key);
    }
}