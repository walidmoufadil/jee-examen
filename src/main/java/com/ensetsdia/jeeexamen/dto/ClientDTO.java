package com.ensetsdia.jeeexamen.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private String nom;
    private String email;
    // Les crédits ne seront généralement pas inclus dans le DTO de base du client pour éviter la surchage.
    // Ils peuvent être récupérés via un endpoint spécifique si nécessaire.
}

