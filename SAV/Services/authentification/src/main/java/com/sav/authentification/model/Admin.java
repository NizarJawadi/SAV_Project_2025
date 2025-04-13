
package com.sav.authentification.model;

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
public class Admin extends User {
    private String telephone;
    private Date dateEumbauche ;
}
