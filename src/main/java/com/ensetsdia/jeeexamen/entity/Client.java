package com.ensetsdia.jeeexamen.entity;

import com.ensetsdia.jeeexamen.entity.Credit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String email;
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Credit> credits;
}