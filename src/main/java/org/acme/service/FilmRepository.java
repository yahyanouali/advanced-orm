package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.acme.entities.Film;

import java.util.List;

@Slf4j
@ApplicationScoped
public class FilmRepository {

    @Inject
    EntityManager em;

    public List<Film> getFilms() {
        return em.createQuery("from Film", Film.class).getResultList();
    }

    public Film getFilmById(Integer id) {
        return em.createQuery("select f from Film f where f.id = ?1", Film.class)
                .setParameter(1, id)
                .getSingleResult();
    }

    @Transactional
    public void saveFilm(Film film) {
        em.persist(film);
    }

    @Transactional
    public void updateFilm(Film film) {
        em.merge(film);
    }

    @Transactional
    public void deleteFilm(Integer id) {
        Film film = em.find(Film.class, id);
        if (film != null) {
            em.remove(film);
        } else {
            log.warn("Le film avec l'id {} n'existe pas", id);
        }
    }
}
