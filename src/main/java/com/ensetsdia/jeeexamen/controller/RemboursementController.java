package com.ensetsdia.jeeexamen.controller;

import com.ensetsdia.jeeexamen.entity.Remboursement;
import com.ensetsdia.jeeexamen.repository.CreditRepository;
import com.ensetsdia.jeeexamen.repository.RemboursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/remboursements")
public class RemboursementController {

    @Autowired
    private RemboursementRepository remboursementRepository;

    @Autowired
    private CreditRepository creditRepository; // Injecter pour vérifier l'existence du crédit

    @PostMapping
    public ResponseEntity<Remboursement> createRemboursement(@RequestBody Remboursement remboursement) {
        // Il serait bon de valider que le crédit associé existe
        if (remboursement.getCredit() == null || !creditRepository.existsById(remboursement.getCredit().getId())) {
            return ResponseEntity.badRequest().body(null); // Ou une réponse d'erreur plus spécifique
        }
        Remboursement savedRemboursement = remboursementRepository.save(remboursement);
        return new ResponseEntity<>(savedRemboursement, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Remboursement> getRemboursementById(@PathVariable Long id) {
        Optional<Remboursement> remboursement = remboursementRepository.findById(id);
        return remboursement.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Remboursement> getAllRemboursements() {
        return remboursementRepository.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Remboursement> updateRemboursement(@PathVariable Long id, @RequestBody Remboursement remboursementDetails) {
        Optional<Remboursement> optionalRemboursement = remboursementRepository.findById(id);
        if (optionalRemboursement.isPresent()) {
            Remboursement remboursement = optionalRemboursement.get();
            remboursement.setDate(remboursementDetails.getDate());
            remboursement.setMontant(remboursementDetails.getMontant());
            remboursement.setType(remboursementDetails.getType());
            // Valider que le crédit associé existe si on permet de le changer
            if (remboursementDetails.getCredit() != null && !creditRepository.existsById(remboursementDetails.getCredit().getId())) {
                 return ResponseEntity.badRequest().body(null);
            }
            remboursement.setCredit(remboursementDetails.getCredit());
            Remboursement updatedRemboursement = remboursementRepository.save(remboursement);
            return ResponseEntity.ok(updatedRemboursement);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRemboursement(@PathVariable Long id) {
        if (remboursementRepository.existsById(id)) {
            remboursementRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

