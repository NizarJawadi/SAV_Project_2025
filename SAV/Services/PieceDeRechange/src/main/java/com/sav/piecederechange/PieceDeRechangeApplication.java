package com.sav.piecederechange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class PieceDeRechangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PieceDeRechangeApplication.class, args);
    }

}
