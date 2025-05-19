package com.ensetsdia.jeeexamen.repository;


import com.ensetsdia.jeeexamen.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
