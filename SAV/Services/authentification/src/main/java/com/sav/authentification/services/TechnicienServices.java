package com.sav.authentification.services;

import com.sav.authentification.model.Roles;
import com.sav.authentification.model.Technicien;
import com.sav.authentification.model.User;

import java.util.List;

public interface TechnicienServices {

    public void addTechnicien(Technicien t);

    public Technicien getTechnicienById(Long id);

    public List<Technicien> getAllTechniciens();

    public void deleteTechnicien(Long id);

    public List<Technicien> getTechnicienBySpecialite(String specialite);

    public List<String> getAllSpecialites() ;




}
