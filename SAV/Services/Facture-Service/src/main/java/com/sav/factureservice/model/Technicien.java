package com.sav.factureservice.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Technicien {
    private Long idUser;
    private String email;
    private String username;
    private String login;
    private String specialite;
    private String adresse;
    private String telephone;
    private double tarifParHeure;
    private Date dateEumbauche ;
    private String matricule;
    private String disponibilite;
}
