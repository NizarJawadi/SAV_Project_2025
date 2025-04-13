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
public class ResponsableSAV extends User {
    private String regionResponsable ;
    private Date priseEnFonction ;
}
