package com.sav.authentification.controller;

import com.sav.authentification.model.ResponsableSAV;
import com.sav.authentification.model.Roles;
import com.sav.authentification.model.Technicien;
import com.sav.authentification.model.User;
import com.sav.authentification.services.ResponsableSavServicesImpl;
import com.sav.authentification.services.TechnicienServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ResponsableSav")

public class ResponsableSavController {

        @Autowired
        private ResponsableSavServicesImpl responsableSavServices ;


        @PostMapping("add")
        void addTechnician(@RequestBody ResponsableSAV res) {
            res.setRole(Roles.RESPONSABLE_SAV);  // Associer le rôle RESPONSABLE_SAV au nouvel utilisateur
            responsableSavServices.addResponsableSav(res);
        }




        //@Secured("ADMIN") // Autoriser uniquement les utilisateurs avec le rôle "ROLE_USER"
        @GetMapping(path = "responsables")
        List<ResponsableSAV> getAllResponsables() {
            return responsableSavServices.getAllResponsables();
        }

        @GetMapping("/{id}")
        User getUserById(@PathVariable Long id ) {
            return responsableSavServices.getResponsableSavById(id);
        }

        @DeleteMapping("/remove/{id}")
        void deleteUser (@PathVariable Long id) {
            responsableSavServices.deleteResponsableSav(id);
        }


    }
