package org.acme.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Getter
@Setter
@Entity
public class Film {
    @Id
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

    @ManyToOne
    @JoinColumn(name = "id_realisateur")
    private Artiste idRealisateur;

}
