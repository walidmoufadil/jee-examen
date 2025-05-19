package com.ensetsdia.jeeexamen.service;

import com.ensetsdia.jeeexamen.dto.RemboursementDTO;
import com.ensetsdia.jeeexamen.entity.Credit;
import com.ensetsdia.jeeexamen.entity.Remboursement;
import com.ensetsdia.jeeexamen.repository.CreditRepository;
import com.ensetsdia.jeeexamen.repository.RemboursementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RemboursementServiceImpl implements RemboursementService {

    @Autowired
    private RemboursementRepository remboursementRepository;

    @Autowired
    private CreditRepository creditRepository; // Pour associer le remboursement à un crédit

    // --- Mapper Methods --- //
    private RemboursementDTO toRemboursementDTO(Remboursement remboursement) {
        if (remboursement == null) return null;
        RemboursementDTO dto = new RemboursementDTO();
        dto.setId(remboursement.getId());
        dto.setDate(remboursement.getDate());
        dto.setMontant(remboursement.getMontant());
        dto.setType(remboursement.getType());
        if (remboursement.getCredit() != null) {
            dto.setCreditId(remboursement.getCredit().getId());
        }
        return dto;
    }

    private Remboursement toRemboursementEntity(RemboursementDTO remboursementDTO) {
        if (remboursementDTO == null) return null;
        Remboursement remboursement = new Remboursement();
        remboursement.setId(remboursementDTO.getId());
        remboursement.setDate(remboursementDTO.getDate());
        remboursement.setMontant(remboursementDTO.getMontant());
        remboursement.setType(remboursementDTO.getType());

        if (remboursementDTO.getCreditId() != null) {
            Credit credit = creditRepository.findById(remboursementDTO.getCreditId())
                    .orElseThrow(() -> new EntityNotFoundException("Crédit non trouvé avec l'id : " + remboursementDTO.getCreditId()));
            remboursement.setCredit(credit);
        }
        return remboursement;
    }

    // --- Service Methods --- //

    @Override
    public RemboursementDTO createRemboursement(RemboursementDTO remboursementDTO) {
        if (remboursementDTO.getCreditId() == null) {
            throw new IllegalArgumentException("L'ID du crédit ne peut pas être nul pour créer un remboursement.");
        }
        Remboursement remboursement = toRemboursementEntity(remboursementDTO);
        remboursement = remboursementRepository.save(remboursement);
        return toRemboursementDTO(remboursement);
    }

    @Override
    public RemboursementDTO getRemboursementById(Long id) {
        Remboursement remboursement = remboursementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Remboursement non trouvé avec l'id : " + id));
        return toRemboursementDTO(remboursement);
    }

    @Override
    public List<RemboursementDTO> getAllRemboursements() {
        return remboursementRepository.findAll().stream()
                .map(this::toRemboursementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RemboursementDTO updateRemboursement(Long id, RemboursementDTO remboursementDTO) {
        Remboursement remboursement = remboursementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Remboursement non trouvé avec l'id : " + id));

        remboursement.setDate(remboursementDTO.getDate());
        remboursement.setMontant(remboursementDTO.getMontant());
        remboursement.setType(remboursementDTO.getType());

        if (remboursementDTO.getCreditId() != null) {
            Credit credit = creditRepository.findById(remboursementDTO.getCreditId())
                    .orElseThrow(() -> new EntityNotFoundException("Crédit non trouvé avec l'id : " + remboursementDTO.getCreditId()));
            remboursement.setCredit(credit);
        } else {
            // Décidez si un remboursement peut exister sans crédit ou si cela devrait lever une erreur.
            // Pour cet exemple, nous permettons de le dissocier, mais cela peut ne pas être souhaitable.
            remboursement.setCredit(null);
        }

        remboursement = remboursementRepository.save(remboursement);
        return toRemboursementDTO(remboursement);
    }

    @Override
    public void deleteRemboursement(Long id) {
        if (!remboursementRepository.existsById(id)) {
            throw new EntityNotFoundException("Remboursement non trouvé avec l'id : " + id);
        }
        remboursementRepository.deleteById(id);
    }

    @Override
    public List<RemboursementDTO> getRemboursementsByCreditId(Long creditId) {
        if (!creditRepository.existsById(creditId)) {
            throw new EntityNotFoundException("Crédit non trouvé avec l'id : " + creditId);
        }
        List<Remboursement> remboursements = remboursementRepository.findAll().stream()
                .filter(r -> r.getCredit() != null && r.getCredit().getId().equals(creditId))
                .collect(Collectors.toList());
        return remboursements.stream()
                .map(this::toRemboursementDTO)
                .collect(Collectors.toList());
    }
}

