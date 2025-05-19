package com.ensetsdia.jeeexamen.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("Professionnel")
public class CreditProfessionnel extends Credit {
    private String motif;
    private String raisonSociale;
}
