package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.acme.entities.Artiste;

import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class ArtisteService {

    @Inject
    EntityManager em;

    public List<Artiste> getArtistes() {
        TypedQuery<Artiste> query = em.createQuery("from Artiste", Artiste.class);
        return query.getResultList();
    }

    public Optional<Artiste> getArtisteById(Integer id) {
        TypedQuery<Artiste> query = em.createQuery("select a from Artiste a where a.id = ?1", Artiste.class);
        query.setParameter(1, id);
        return Optional.of(query.getSingleResult());
    }
}