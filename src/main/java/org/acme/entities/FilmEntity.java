package org.acme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Film")
public class FilmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "titre", nullable = false, length = 50)
    private String titre;

    @Column(name = "annee", nullable = false)
    private Integer annee;

    @Column(name = "genre", nullable = false, length = 30)
    private String genre;

    @Column(name = "resume", columnDefinition = "text")
    private String resume;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_realisateur")
    @JsonIgnore
    private ArtisteEntity realisateur;

}
