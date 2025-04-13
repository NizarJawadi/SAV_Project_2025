package com.sav.authentification.services;


import com.sav.authentification.model.ResponsableSAV;
import com.sav.authentification.model.Technicien;
import com.sav.authentification.repository.ResponsableSAVRepository;
import com.sav.authentification.repository.TechnicienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponsableSavServicesImpl implements  ResponsableSavServices {

        private final ResponsableSAVRepository repository;
        private final PasswordEncoder passwordEncoder;


        @Autowired
        public ResponsableSavServicesImpl(@Lazy ResponsableSAVRepository responsableSAVRepository, PasswordEncoder passwordEncoder) {
            this.repository = responsableSAVRepository;
            this.passwordEncoder = passwordEncoder;
        }
        @Override
        public void addResponsableSav(ResponsableSAV res) {
            res.setPassword(passwordEncoder.encode(res.getPassword()));
            repository.save(res);
        }

        @Override
        public ResponsableSAV getResponsableSavById(Long id) {
            return repository.findById(id).orElse(null);
        }

        @Override
        public List<ResponsableSAV> getAllResponsables() {
            return repository.findAll();
        }

        @Override
        public void deleteResponsableSav(Long id) {
            repository.deleteById(id);
        }

    }
