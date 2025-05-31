package org.acme.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Genre")
public class GenreEntity {
    @Id
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    //TODO [Reverse Engineering] generate columns from DB
}