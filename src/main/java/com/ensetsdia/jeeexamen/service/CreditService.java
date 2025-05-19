package com.ensetsdia.jeeexamen.service;

import com.ensetsdia.jeeexamen.dto.CreditDTO;
import com.ensetsdia.jeeexamen.dto.RemboursementDTO;

import java.util.List;

public interface CreditService {
    CreditDTO createCredit(CreditDTO creditDTO);
    CreditDTO getCreditById(Long id);
    List<CreditDTO> getAllCredits();
    CreditDTO updateCredit(Long id, CreditDTO creditDTO);
    void deleteCredit(Long id);
    List<RemboursementDTO> getCreditRemboursements(Long creditId);
}

