package com.ensetsdia.jeeexamen.dto;

import com.ensetsdia.jeeexamen.entity.TypeRemboursement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemboursementDTO {
    private Long id;
    private Date date;
    private double montant;
    private TypeRemboursement type;
    private Long creditId; // ID du crédit associé
}

