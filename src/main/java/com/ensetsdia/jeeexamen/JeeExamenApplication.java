package com.ensetsdia.jeeexamen;

import com.ensetsdia.jeeexamen.entity.*;
import com.ensetsdia.jeeexamen.repository.ClientRepository;
import com.ensetsdia.jeeexamen.repository.CreditRepository;
import com.ensetsdia.jeeexamen.repository.RemboursementRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class JeeExamenApplication {

    public static void main(String[] args) {
        SpringApplication.run(JeeExamenApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(ClientRepository clientRepository,
                                        CreditRepository creditRepository,
                                        RemboursementRepository remboursementRepository) {
        return args -> {
            Client client1 = new Client(null, "Walid Moufadil", "walid.moufadil@gmail.com", null);
            Client client2 = new Client(null, "Said Afsin", "said.afsin@gmail.com", null);
            clientRepository.saveAll(List.of(client1, client2));

            Credit credit1 = new Credit();
            credit1.setDateDemande(new Date());
            credit1.setStatut(Statut.Accepte);
            credit1.setDateAcceptation(new Date());
            credit1.setMontant(50000);
            credit1.setDureeRemboursement(60);
            credit1.setTauxInteret(2.5);
            credit1.setClient(client1);
            creditRepository.save(credit1);

            Remboursement remboursement1 = new Remboursement();
            remboursement1.setDate(new Date());
            remboursement1.setMontant(850);
            remboursement1.setType(TypeRemboursement.Mensualite);
            remboursement1.setCredit(credit1);
            remboursementRepository.save(remboursement1);


            CreditImmobilier creditImmobilier = new CreditImmobilier();
            creditImmobilier.setDateDemande(new Date());
            creditImmobilier.setStatut(Statut.Accepte);
            creditImmobilier.setDateAcceptation(new Date());
            creditImmobilier.setMontant(150000);
            creditImmobilier.setDureeRemboursement(120);
            creditImmobilier.setTauxInteret(1.8);
            creditImmobilier.setClient(client2);
            creditImmobilier.setTypeBien(TypeBien.Appartement);
            creditRepository.save(creditImmobilier);

            printClients(clientRepository, creditRepository, remboursementRepository);
        };
    }

    @Transactional
    public void printClients(ClientRepository clientRepository,
                             CreditRepository creditRepository,
                             RemboursementRepository remboursementRepository) {
        clientRepository.findAll().forEach(c -> {
            System.out.println("Client => " + c.toString());
        });

        creditRepository.findAll().forEach(credit -> {
            System.out.println("Credit => " + credit.toString());
        });

        remboursementRepository.findAll().forEach(remboursement -> {
            System.out.println("Remboursement => " + remboursement.toString());
        });
    }
}
