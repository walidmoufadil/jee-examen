package com.ensetsdia.jeeexamen.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@ToString
public class Credit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateDemande;
    private Statut statut;
    private Date dateAcceptation;
    private double montant;
    private int dureeRemboursement;
    private double tauxInteret;
    @ManyToOne
    @ToString.Exclude
    private com.ensetsdia.jeeexamen.entity.Client client;
    @OneToMany(mappedBy = "credit",fetch = FetchType.EAGER)
    private List<Remboursement> remboursements;
}
