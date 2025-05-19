package com.ensetsdia.jeeexamen.controller;

import com.ensetsdia.jeeexamen.dto.ClientDTO;
import com.ensetsdia.jeeexamen.dto.CreditDTO;
import com.ensetsdia.jeeexamen.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        ClientDTO savedClient = clientService.createClient(clientDTO);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(client); // La gestion NotFound est dans le service
    }

    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        ClientDTO updatedClient = clientService.updateClient(id, clientDTO);
        return ResponseEntity.ok(updatedClient); // La gestion NotFound est dans le service
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id); // La gestion NotFound est dans le service
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/credits")
    public ResponseEntity<List<CreditDTO>> getClientCredits(@PathVariable Long id) {
        List<CreditDTO> credits = clientService.getClientCredits(id);
        return ResponseEntity.ok(credits); // La gestion NotFound est dans le service
    }
}
