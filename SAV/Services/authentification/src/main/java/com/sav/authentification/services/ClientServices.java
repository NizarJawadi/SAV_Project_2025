package com.sav.authentification.services;

import com.sav.authentification.model.Admin;
import com.sav.authentification.model.Client;

import java.util.List;

public interface ClientServices {

    public void addClient(Client client);

    public Client getClientById(Long id);

    public List<Client> getAllClients();

    public void deleteClient(Long id);

    public void updatePassword(String email, String newPassword);

    public boolean existsByEmail(String email);
}
