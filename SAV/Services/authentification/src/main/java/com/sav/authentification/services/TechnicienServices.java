package com.sav.authentification.services;

import com.sav.authentification.model.Roles;
import com.sav.authentification.model.Technicien;
import com.sav.authentification.model.User;

import java.util.List;
import java.util.Map;

public interface TechnicienServices {

    public void addTechnicien(Technicien t);

    public Technicien getTechnicienById(Long id);

    public List<Technicien> getAllTechniciens();

    public void deleteTechnicien(Long id);

    public List<Technicien> getTechnicienBySpecialite(String specialite);

    public List<String> getAllSpecialites() ;

    public Map<String, Long> countTechniciensBySpecialite() ;

    public Technicien updateTechnicien(Long id, Technicien technicien) ;

    Technicien getTechnicienByMatricule(String matricule);


}
