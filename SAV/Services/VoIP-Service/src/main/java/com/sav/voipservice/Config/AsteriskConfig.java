package com.sav.voipservice.Config;

import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsteriskConfig {
    @Bean
    public ManagerConnection managerConnection() {
        ManagerConnectionFactory factory = new ManagerConnectionFactory("192.168.66.136", "admin", "191900");
        return factory.createManagerConnection();
    }
}
