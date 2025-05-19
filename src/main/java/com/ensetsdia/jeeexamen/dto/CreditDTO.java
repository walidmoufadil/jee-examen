package com.ensetsdia.jeeexamen.dto;

import com.ensetsdia.jeeexamen.entity.Statut;
import com.ensetsdia.jeeexamen.entity.TypeBien;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditDTO {
    private Long id;
    private Date dateDemande;
    private Statut statut;
    private Date dateAcceptation;
    private double montant;
    private int dureeRemboursement;
    private double tauxInteret;
    private Long clientId; // ID du client associé
    private String typeCredit; // Pour distinguer les types de crédit (Personnel, Immobilier, Professionnel)

    // Champs spécifiques aux sous-types de crédit
    private String motif; // Pour CreditPersonnel et CreditProfessionnel
    private TypeBien typeBien; // Pour CreditImmobilier
    private String raisonSociale; // Pour CreditProfessionnel
}

