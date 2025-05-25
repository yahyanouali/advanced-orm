package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.acme.entities.Artiste;

import java.util.List;

@Slf4j
@ApplicationScoped
public class ArtisteService {

    @Inject
    EntityManager em;

    public List<Artiste> getArtistes() {
        TypedQuery<Artiste> query = em.createQuery("from Artiste", Artiste.class);
        return query.getResultList();
    }
}