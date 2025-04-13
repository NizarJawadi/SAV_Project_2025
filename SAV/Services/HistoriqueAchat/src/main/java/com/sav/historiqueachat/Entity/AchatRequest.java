package com.sav.historiqueachat.Entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class AchatRequest {

    private Long clientId;
    private Long produitId;
    private int quantite;
    private Date garantieExpireLe ;
    private float prixUnitaire;
}
