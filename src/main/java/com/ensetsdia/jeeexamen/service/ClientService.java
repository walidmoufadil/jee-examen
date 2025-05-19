package com.ensetsdia.jeeexamen.service;

import com.ensetsdia.jeeexamen.dto.ClientDTO;
import com.ensetsdia.jeeexamen.dto.CreditDTO;

import java.util.List;

public interface ClientService {
    ClientDTO createClient(ClientDTO clientDTO);
    ClientDTO getClientById(Long id);
    List<ClientDTO> getAllClients();
    ClientDTO updateClient(Long id, ClientDTO clientDTO);
    void deleteClient(Long id);
    List<CreditDTO> getClientCredits(Long clientId);
}

