package com.sav.authentification.services;


import com.sav.authentification.model.Admin;
import com.sav.authentification.model.Client;
import com.sav.authentification.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

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
        if(client.getNumSIP() == null || client.getNumSIP().isEmpty()) {
            client.setNumSIP(generateUniqueSIPNumber());
        }
        repository.save(client);
    }


    public boolean existsByNumSIP(String numSIP) {
        return repository.existsByNumSIP(numSIP);
    }

    public String generateUniqueSIPNumber() {
        Random random = new Random();
        String generatedSIP;

        do {
            int randomNum = 7001 + random.nextInt(999); // 7001-7999
            generatedSIP = String.valueOf(randomNum);
        } while (existsByNumSIP(generatedSIP));

        return generatedSIP;
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

    public boolean existsByEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }

    public void updatePassword(String email, String newPassword) {
        Client client = repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));

        // Ici vous devriez hasher le mot de passe
        client.setPassword(passwordEncoder.encode(newPassword));
        repository.save(client);
    }

}
