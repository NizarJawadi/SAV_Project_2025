package com.sav.voipservice.Config;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

@Configuration
public class AmiConfig {

    @Value("${asterisk.ami.socket-timeout:5000}")
    private int socketTimeout;

    @Bean
    public ManagerConnection managerConnection(
            @Value("${asterisk.ami.host}") String host,
            @Value("${asterisk.ami.port}") int port,
            @Value("${asterisk.ami.username}") String username,
            @Value("${asterisk.ami.password}") String password) {

        ManagerConnectionFactory factory = new ManagerConnectionFactory(
                host, port, username, password
        );

        ManagerConnection connection = factory.createManagerConnection();

        // Seules méthodes timeout disponibles dans 3.38.0 :
        connection.setSocketTimeout(socketTimeout);

        return connection;
    }

    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return converter;
    }
}