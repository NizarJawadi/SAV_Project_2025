package com.sav.authentification.services;

import com.sav.authentification.model.Admin;
import com.sav.authentification.model.Technicien;
import com.sav.authentification.repository.AdminRepository;
import com.sav.authentification.repository.TechnicienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminServiceImpl implements AdminServices{


        private final AdminRepository repository;
        private final PasswordEncoder passwordEncoder;


        @Autowired
        public AdminServiceImpl(@Lazy AdminRepository  adminRepository, PasswordEncoder passwordEncoder) {
            this.repository = adminRepository;
            this.passwordEncoder = passwordEncoder;
        }
        @Override
        public void addAdmin(Admin a) {
            a.setPassword(passwordEncoder.encode(a.getPassword()));
            repository.save(a);
        }

        @Override
        public Admin getAdminById(Long id) {
            return repository.findById(id).orElse(null);
        }

        @Override
        public List<Admin> getAllAdmins() {
            return repository.findAll();
        }

        @Override
        public void deleteAdmin(Long id) {
            repository.deleteById(id);
        }

    }
