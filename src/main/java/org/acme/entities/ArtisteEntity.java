package org.acme.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class ArtisteEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 30)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 30)
    private String prenom;

    @Column(name = "annee_naissance")
    private Integer anneeNaissance;

}