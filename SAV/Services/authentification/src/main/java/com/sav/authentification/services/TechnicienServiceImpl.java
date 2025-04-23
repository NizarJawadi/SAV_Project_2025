package com.sav.authentification.services;

import com.sav.authentification.model.Roles;
import com.sav.authentification.model.Technicien;
import com.sav.authentification.model.User;
import com.sav.authentification.repository.TechnicienRepository;
import com.sav.authentification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class TechnicienServiceImpl implements TechnicienServices{

    private final TechnicienRepository repository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public TechnicienServiceImpl(@Lazy TechnicienRepository technicienRepository, PasswordEncoder passwordEncoder) {
        this.repository = technicienRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void addTechnicien(Technicien t) {
        t.setPassword(passwordEncoder.encode(t.getPassword()));
        repository.save(t);
    }
    @Override
    public Technicien updateTechnicien(Long id, Technicien technicien) {
        if (!repository.existsById(id)) {
            return null; // Retourner null si le technicien n'existe pas
        }
        technicien.setIdUser(id); // Assurer que l'ID est correct
        return repository.save(technicien); // Sauvegarder les modifications
    }

    @Override
    public Technicien getTechnicienById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Technicien> getAllTechniciens() {
        return repository.findAll();
    }

    @Override
    public void deleteTechnicien(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Technicien> getTechnicienBySpecialite(String specialite) {

        return  repository.findBySpecialite(specialite);
    }


    public List<String> getAllSpecialites() {
        return repository.findAllSpecialites();
    }


    public Map<String, Long> countTechniciensBySpecialite() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(Technicien::getSpecialite, Collectors.counting()));
    }

    @Override
    public Technicien getTechnicienByMatricule(String matricule) {
        return repository.findByMatricule(matricule).orElse(null);
    }

}
