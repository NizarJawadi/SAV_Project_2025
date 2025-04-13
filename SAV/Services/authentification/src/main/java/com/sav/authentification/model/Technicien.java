package com.sav.authentification.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DiscriminatorValue("TECHNICIEN")
public class Technicien extends User {
    private String specialite;
    private String adresse;
    private String telephone;
    private double tarifParHeure;
    private Date dateEumbauche ;
    private String matricule;
    private String disponibilite;
}
