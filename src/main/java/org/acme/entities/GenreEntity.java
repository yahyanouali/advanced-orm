package org.acme.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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