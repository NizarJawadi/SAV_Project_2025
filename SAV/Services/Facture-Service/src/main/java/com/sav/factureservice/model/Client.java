package com.sav.factureservice.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client {
    private Long idUser;
    private String email;
    private String username;
    private String login;
    private String ville;
    private String codePostal;
    private String telephone;
    private LocalDate dateInscription;
}
