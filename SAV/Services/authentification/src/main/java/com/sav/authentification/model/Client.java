
package com.sav.authentification.model;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Client extends User {
    private String ville;
    private String codePostal;
    private String telephone;
    private LocalDate dateInscription;
}
