package com.ensetsdia.jeeexamen.service;

import com.ensetsdia.jeeexamen.dto.ClientDTO;
import com.ensetsdia.jeeexamen.dto.CreditDTO;
import com.ensetsdia.jeeexamen.entity.Client;
import com.ensetsdia.jeeexamen.entity.Credit;
import com.ensetsdia.jeeexamen.entity.CreditImmobilier;
import com.ensetsdia.jeeexamen.entity.CreditPersonnel;
import com.ensetsdia.jeeexamen.entity.CreditProfessionnel;
import com.ensetsdia.jeeexamen.repository.ClientRepository;
import com.ensetsdia.jeeexamen.repository.CreditRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CreditRepository creditRepository; // Utilisé pour getClientCredits

    // --- Mapper Methods --- //
    private ClientDTO toClientDTO(Client client) {
        if (client == null) return null;
        return new ClientDTO(client.getId(), client.getNom(), client.getEmail());
    }

    private Client toClientEntity(ClientDTO clientDTO) {
        if (clientDTO == null) return null;
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setNom(clientDTO.getNom());
        client.setEmail(clientDTO.getEmail());
        // La liste des crédits n'est pas gérée ici pour la création/mise à jour simple du client
        return client;
    }

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
            dto.setTypeCredit("General");
        }
        return dto;
    }

    // --- Service Methods --- //

    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = toClientEntity(clientDTO);
        // La gestion des crédits lors de la création d'un client peut être ajoutée ici si nécessaire
        client = clientRepository.save(client);
        return toClientDTO(client);
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé avec l'id : " + id));
        return toClientDTO(client);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::toClientDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé avec l'id : " + id));
        client.setNom(clientDTO.getNom());
        client.setEmail(clientDTO.getEmail());
        // La mise à jour des crédits via ce DTO n'est pas gérée pour la simplicité
        client = clientRepository.save(client);
        return toClientDTO(client);
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client non trouvé avec l'id : " + id);
        }
        // Gérer la logique de suppression des crédits associés si nécessaire avant de supprimer le client
        // Par exemple, vérifier s'il y a des crédits et décider de la stratégie (suppression en cascade, erreur, etc.)
        // Pour l'instant, on suppose que la configuration de la base de données ou une logique séparée gère cela.
        clientRepository.deleteById(id);
    }

    @Override
    public List<CreditDTO> getClientCredits(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé avec l'id : " + clientId));
        // Si la relation est LAZY, cela va charger les crédits.
        // Si EAGER, ils sont déjà chargés mais il est bon de passer par le repository pour plus de contrôle ou des requêtes spécifiques.
        // List<Credit> credits = client.getCredits(); // Si EAGER et simple récupération
        List<Credit> credits = creditRepository.findAll().stream()
            .filter(credit -> credit.getClient() != null && credit.getClient().getId().equals(clientId))
            .collect(Collectors.toList());
        return credits.stream()
                .map(this::toCreditDTO)
                .collect(Collectors.toList());
    }
}

