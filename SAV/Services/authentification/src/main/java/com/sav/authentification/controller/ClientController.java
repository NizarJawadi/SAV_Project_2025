package com.sav.authentification.controller;

import com.sav.authentification.model.Admin;
import com.sav.authentification.model.Client;
import com.sav.authentification.model.Roles;
import com.sav.authentification.services.AdminServiceImpl;
import com.sav.authentification.services.ClientServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("client")
public class ClientController {

        @Autowired
        private ClientServicesImpl clientService ;

        @PostMapping
        // Ajouter un Admin
        void addClient(@RequestBody Client client) {
            client.setRole(Roles.CLIENT);  // Associer le rôle "CLIENT" au nouvel utilisateur
            client.setDateInscription(LocalDate.now());
            clientService.addClient(client);
        }



        @GetMapping(path = "clients")
        List<Client> getAllClients() {
            return clientService.getAllClients();
        }

        @GetMapping("/{id}")
        Client getClientById(@PathVariable Long id ) {
            return clientService.getClientById(id);
        }

        @DeleteMapping("/remove/{id}")
        void deleteClient (@PathVariable Long id) {
            clientService.deleteClient(id);
        }


    }


