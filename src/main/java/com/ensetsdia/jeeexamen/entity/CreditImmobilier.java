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
@DiscriminatorValue("Immobilier")
public class CreditImmobilier extends Credit {
    private TypeBien typeBien;
}
