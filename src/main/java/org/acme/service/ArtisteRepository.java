package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.acme.entities.ArtisteEntity;

import java.util.List;

@Slf4j
@ApplicationScoped
public class ArtisteRepository {

    @Inject
    EntityManager em;

    public List<ArtisteEntity> getArtistes() {
        TypedQuery<ArtisteEntity> query = em.createQuery("from ArtisteEntity", ArtisteEntity.class);
        return query.getResultList();
    }

    public ArtisteEntity getArtisteById(Integer id) {
        return em.createQuery("select a from ArtisteEntity a where a.id = ?1", ArtisteEntity.class)
                .setParameter(1, id)
                .getSingleResult();
    }

    public ArtisteEntity getArtisteByNomPrenom(String nom, String prenom) {
        TypedQuery<ArtisteEntity> query = em.createQuery("select a from ArtisteEntity a where a.nom = ?1 and a.prenom = ?2",
                        ArtisteEntity.class)
                .setParameter(1, nom)
                .setParameter(2, prenom);

        return query.getSingleResult();
    }

    @Transactional
    public void saveArtiste(ArtisteEntity artisteEntity) {
        em.persist(artisteEntity);
    }

    @Transactional
    public void updateArtiste(ArtisteEntity artisteEntity) {
        em.merge(artisteEntity);
    }

    @Transactional
    public void deleteArtiste(Integer id) {
        ArtisteEntity artisteEntity = em.find(ArtisteEntity.class, id);
        if (artisteEntity != null) {
            em.remove(artisteEntity);
        } else {
            log.warn("L'artiste avec l'id {} n'existe pas", id);
        }
    }

}