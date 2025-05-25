package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.acme.entities.Artiste;

import java.util.List;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class ArtisteRepository {

    @Inject
    EntityManager em;

    public List<Artiste> getArtistes() {
        TypedQuery<Artiste> query = em.createQuery("from Artiste", Artiste.class);
        return query.getResultList();
    }

    public Artiste getArtisteById(Integer id) {
        return em.createQuery("select a from Artiste a where a.id = ?1", Artiste.class)
                .setParameter(1, id)
                .getSingleResult();
    }

    public Artiste getArtisteByNomPrenom(String nom, String prenom) {
        TypedQuery<Artiste> query = em.createQuery("select a from Artiste a where a.nom = ?1 and a.prenom = ?2",
                        Artiste.class)
                .setParameter(1, nom)
                .setParameter(2, prenom);

        return query.getSingleResult();
    }

    @Transactional
    public void saveArtiste(Artiste artiste) {
        em.persist(artiste);
    }

    @Transactional
    public void updateArtiste(Artiste artiste) {
        em.merge(artiste);
    }

    @Transactional
    public void deleteArtiste(Integer id) {
        Artiste artiste = em.find(Artiste.class, id);
        if (artiste != null) {
            em.remove(artiste);
        } else {
            log.warn("L'artiste avec l'id {} n'existe pas", id);
        }
    }

}