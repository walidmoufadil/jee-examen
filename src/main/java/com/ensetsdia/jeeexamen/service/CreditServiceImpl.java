package com.ensetsdia.jeeexamen.service;

import com.ensetsdia.jeeexamen.dto.CreditDTO;
import com.ensetsdia.jeeexamen.dto.RemboursementDTO;
import com.ensetsdia.jeeexamen.entity.*;
import com.ensetsdia.jeeexamen.repository.ClientRepository;
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
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private ClientRepository clientRepository; // Pour associer le crédit à un client

    @Autowired
    private RemboursementRepository remboursementRepository; // Pour gérer les remboursements

    // --- Mapper Methods --- //
    private CreditDTO toCreditDTO(Credit credit) {
        if (credit == null) return null;
        CreditDTO dto = new CreditDTO();
        dto.setId(credit.getId());
        dto.setDateDemande(credit.getDateDemande());
        dto.setStatut(credit.getStatut());
        dto.setDateAcceptation(credit.getDateAcceptation());
        dto.setMontant(credit.getMontant());
        dto.setDureeRemboursement(credit.getDureeRemboursement());
        dto.setTauxInteret(credit.getTauxInteret());
        if (credit.getClient() != null) {
            dto.setClientId(credit.getClient().getId());
        }

        if (credit instanceof CreditPersonnel) {
            dto.setTypeCredit("Personnel");
            dto.setMotif(((CreditPersonnel) credit).getMotif());
        } else if (credit instanceof CreditImmobilier) {
            dto.setTypeCredit("Immobilier");
            dto.setTypeBien(((CreditImmobilier) credit).getTypeBien());
        } else if (credit instanceof CreditProfessionnel) {
            dto.setTypeCredit("Professionnel");
            dto.setMotif(((CreditProfessionnel) credit).getMotif());
            dto.setRaisonSociale(((CreditProfessionnel) credit).getRaisonSociale());
        } else {
            // Cas pour un crédit générique si la hiérarchie n'est pas utilisée ou pour un type non spécifié
            dto.setTypeCredit("General");
        }
        return dto;
    }

    private Credit toCreditEntity(CreditDTO creditDTO) {
        if (creditDTO == null) return null;
        Credit credit;
        // Instancier le type de crédit correct basé sur typeCredit
        // Note: Cette logique suppose que typeCredit est correctement défini dans le DTO
        // et correspond aux DiscriminatorValues des entités.
        switch (creditDTO.getTypeCredit()) {
            case "Personnel":
                CreditPersonnel cp = new CreditPersonnel();
                cp.setMotif(creditDTO.getMotif());
                credit = cp;
                break;
            case "Immobilier":
                CreditImmobilier ci = new CreditImmobilier();
                ci.setTypeBien(creditDTO.getTypeBien());
                credit = ci;
                break;
            case "Professionnel":
                CreditProfessionnel cpro = new CreditProfessionnel();
                cpro.setMotif(creditDTO.getMotif());
                cpro.setRaisonSociale(creditDTO.getRaisonSociale());
                credit = cpro;
                break;
            default:
                // Fallback ou erreur si le type n'est pas géré
                // Pour cet exemple, on crée un Credit de base, mais vous pourriez vouloir lever une exception.
                credit = new Credit();
                // Il faudrait s'assurer que la colonne discriminateur est bien gérée si on arrive ici.
        }

        credit.setId(creditDTO.getId());
        credit.setDateDemande(creditDTO.getDateDemande());
        credit.setStatut(creditDTO.getStatut());
        credit.setDateAcceptation(creditDTO.getDateAcceptation());
        credit.setMontant(creditDTO.getMontant());
        credit.setDureeRemboursement(creditDTO.getDureeRemboursement());
        credit.setTauxInteret(creditDTO.getTauxInteret());

        if (creditDTO.getClientId() != null) {
            Client client = clientRepository.findById(creditDTO.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client non trouvé avec l'id : " + creditDTO.getClientId()));
            credit.setClient(client);
        }
        // La liste des remboursements n'est pas gérée ici pour la création/mise à jour simple du crédit
        return credit;
    }

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

    // --- Service Methods --- //

    @Override
    public CreditDTO createCredit(CreditDTO creditDTO) {
        Credit credit = toCreditEntity(creditDTO);
        credit = creditRepository.save(credit);
        return toCreditDTO(credit);
    }

    @Override
    public CreditDTO getCreditById(Long id) {
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Crédit non trouvé avec l'id : " + id));
        return toCreditDTO(credit);
    }

    @Override
    public List<CreditDTO> getAllCredits() {
        return creditRepository.findAll().stream()
                .map(this::toCreditDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CreditDTO updateCredit(Long id, CreditDTO creditDTO) {
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Crédit non trouvé avec l'id : " + id));

        // Mise à jour des champs communs
        credit.setDateDemande(creditDTO.getDateDemande());
        credit.setStatut(creditDTO.getStatut());
        credit.setDateAcceptation(creditDTO.getDateAcceptation());
        credit.setMontant(creditDTO.getMontant());
        credit.setDureeRemboursement(creditDTO.getDureeRemboursement());
        credit.setTauxInteret(creditDTO.getTauxInteret());

        if (creditDTO.getClientId() != null) {
            Client client = clientRepository.findById(creditDTO.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client non trouvé avec l'id : " + creditDTO.getClientId()));
            credit.setClient(client);
        } else {
            credit.setClient(null); // Permettre de dissocier le client
        }

        // Gérer les champs spécifiques aux sous-types
        // Important : La modification du *type* de crédit (ex: Personnel -> Immobilier) n'est pas gérée ici
        // et nécessiterait une logique plus complexe (suppression et recréation ou conversion).
        if (credit instanceof CreditPersonnel && "Personnel".equals(creditDTO.getTypeCredit())) {
            ((CreditPersonnel) credit).setMotif(creditDTO.getMotif());
        } else if (credit instanceof CreditImmobilier && "Immobilier".equals(creditDTO.getTypeCredit())) {
            ((CreditImmobilier) credit).setTypeBien(creditDTO.getTypeBien());
        } else if (credit instanceof CreditProfessionnel && "Professionnel".equals(creditDTO.getTypeCredit())) {
            ((CreditProfessionnel) credit).setMotif(creditDTO.getMotif());
            ((CreditProfessionnel) credit).setRaisonSociale(creditDTO.getRaisonSociale());
        }
        // Si le typeCredit dans le DTO ne correspond pas au type de l'entité existante,
        // il faudrait décider d'une stratégie (ignorer, erreur, etc.).

        credit = creditRepository.save(credit);
        return toCreditDTO(credit);
    }

    @Override
    public void deleteCredit(Long id) {
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Crédit non trouvé avec l'id : " + id));
        // Gérer la suppression des remboursements associés
        // La configuration CascadeType.REMOVE sur la relation @OneToMany dans Credit vers Remboursement
        // pourrait gérer cela automatiquement. Sinon, il faut le faire manuellement:
        List<Remboursement> remboursements = remboursementRepository.findAll().stream()
            .filter(r -> r.getCredit() != null && r.getCredit().getId().equals(id))
            .collect(Collectors.toList());
        remboursementRepository.deleteAll(remboursements);

        creditRepository.deleteById(id);
    }

    @Override
    public List<RemboursementDTO> getCreditRemboursements(Long creditId) {
        Credit credit = creditRepository.findById(creditId)
                .orElseThrow(() -> new EntityNotFoundException("Crédit non trouvé avec l'id : " + creditId));
        // List<Remboursement> remboursements = credit.getRemboursements(); // Si EAGER
         List<Remboursement> remboursements = remboursementRepository.findAll().stream()
            .filter(r -> r.getCredit() != null && r.getCredit().getId().equals(creditId))
            .collect(Collectors.toList());
        return remboursements.stream()
                .map(this::toRemboursementDTO)
                .collect(Collectors.toList());
    }
}

