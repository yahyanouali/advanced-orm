package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.acme.entities.FilmEntity;

import java.util.List;

@Slf4j
@ApplicationScoped
public class FilmRepository {

    @Inject
    EntityManager em;

    public List<FilmEntity> getFilms() {
        return em.createQuery("from FilmEntity", FilmEntity.class).getResultList();
    }

    public FilmEntity getFilmById(Integer id) {
        return em.createQuery("select f from FilmEntity f where f.id = ?1", FilmEntity.class)
                .setParameter(1, id)
                .getSingleResult();
    }

    @Transactional
    public void saveFilm(FilmEntity filmEntity) {
        em.persist(filmEntity);
    }

    @Transactional
    public void updateFilm(FilmEntity filmEntity) {
        em.merge(filmEntity);
    }

    @Transactional
    public void deleteFilm(Integer id) {
        FilmEntity filmEntity = em.find(FilmEntity.class, id);
        if (filmEntity != null) {
            em.remove(filmEntity);
        } else {
            log.warn("Le film avec l'id {} n'existe pas", id);
        }
    }
}
