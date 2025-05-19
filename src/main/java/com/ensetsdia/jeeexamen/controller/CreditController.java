package com.ensetsdia.jeeexamen.controller;

import com.ensetsdia.jeeexamen.dto.CreditDTO;
import com.ensetsdia.jeeexamen.dto.RemboursementDTO;
import com.ensetsdia.jeeexamen.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
public class CreditController {

    @Autowired
    private CreditService creditService;

    @PostMapping
    public ResponseEntity<CreditDTO> createCredit(@RequestBody CreditDTO creditDTO) {
        CreditDTO savedCredit = creditService.createCredit(creditDTO);
        return new ResponseEntity<>(savedCredit, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditDTO> getCreditById(@PathVariable Long id) {
        CreditDTO credit = creditService.getCreditById(id);
        return ResponseEntity.ok(credit); // La gestion NotFound est dans le service
    }

    @GetMapping
    public List<CreditDTO> getAllCredits() {
        return creditService.getAllCredits();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreditDTO> updateCredit(@PathVariable Long id, @RequestBody CreditDTO creditDTO) {
        CreditDTO updatedCredit = creditService.updateCredit(id, creditDTO);
        return ResponseEntity.ok(updatedCredit); // La gestion NotFound est dans le service
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCredit(@PathVariable Long id) {
        creditService.deleteCredit(id); // La gestion NotFound est dans le service
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/remboursements")
    public ResponseEntity<List<RemboursementDTO>> getCreditRemboursements(@PathVariable Long id) {
        List<RemboursementDTO> remboursements = creditService.getCreditRemboursements(id);
        return ResponseEntity.ok(remboursements); // La gestion NotFound est dans le service
    }
}
