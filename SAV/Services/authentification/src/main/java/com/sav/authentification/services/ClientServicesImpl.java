package com.sav.authentification.services;


import com.sav.authentification.model.Admin;
import com.sav.authentification.model.Client;
import com.sav.authentification.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClientServicesImpl implements  ClientServices {

    private final ClientRepository repository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public ClientServicesImpl(@Lazy ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.repository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void addClient(Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        repository.save(client);
    }

    @Override
    public Client getClientById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Client> getAllClients() {
        return repository.findAll();
    }

    @Override
    public void deleteClient(Long id) {
        repository.deleteById(id);
    }
}
