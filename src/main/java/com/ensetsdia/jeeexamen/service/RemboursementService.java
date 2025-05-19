package com.ensetsdia.jeeexamen.service;

import com.ensetsdia.jeeexamen.dto.RemboursementDTO;

import java.util.List;

public interface RemboursementService {
    RemboursementDTO createRemboursement(RemboursementDTO remboursementDTO);
    RemboursementDTO getRemboursementById(Long id);
    List<RemboursementDTO> getAllRemboursements();
    RemboursementDTO updateRemboursement(Long id, RemboursementDTO remboursementDTO);
    void deleteRemboursement(Long id);
    List<RemboursementDTO> getRemboursementsByCreditId(Long creditId);

}

