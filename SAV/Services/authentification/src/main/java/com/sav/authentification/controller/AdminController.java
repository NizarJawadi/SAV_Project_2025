package com.sav.authentification.controller;

import com.sav.authentification.model.Admin;
import com.sav.authentification.model.Roles;
import com.sav.authentification.services.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {


        @Autowired
        private AdminServiceImpl adminService ;


        // Ajouter un Admin
        @PostMapping("add")
        void addAdmin(@RequestBody Admin a) {
            a.setRole(Roles.ADMIN);  // Associer le rôle "Admin" au nouvel utilisateur
            adminService.addAdmin(a);
        }


        @GetMapping(path = "admins")
        List<Admin> getAllAdmins() {
            return adminService.getAllAdmins();
        }

        @GetMapping("/{id}")
        Admin getAdminById(@PathVariable Long id ) {
            return adminService.getAdminById(id);
        }

        @DeleteMapping("/remove/{id}")
        void deleteAdmin (@PathVariable Long id) {
            adminService.deleteAdmin(id);
        }


}
