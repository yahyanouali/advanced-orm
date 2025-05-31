package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.acme.entities.FilmEntity;
import org.acme.service.FilmRepository;

import java.util.List;

@Path("/films")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class FilmResource {

    @Inject
    FilmRepository filmRepository;

    @GET
    public List<FilmEntity> getFilms() {
        var films = filmRepository.getFilms();
        log.info("Films trouvés : {}", films);
        return films;
    }

    @GET
    @Path("/{id}")
    public FilmEntity getFilmById(@PathParam("id") Integer id) {
        var film = filmRepository.getFilmById(id);
        log.info("Film trouvé : {}", film);
        return film;
    }

    @POST
    public void createFilm(FilmEntity filmEntity) {
        log.info("Création du film : {}", filmEntity);
        filmRepository.saveFilm(filmEntity);
    }

    @PUT
    public void updateFilm(FilmEntity filmEntity) {
        log.info("Mise à jour du film : {}", filmEntity);
        filmRepository.updateFilm(filmEntity);
    }

    @DELETE
    @Path("/{id}")
    public void deleteFilm(@PathParam("id") Integer id) {
        log.info("Suppression du film avec l'id : {}", id);
        filmRepository.deleteFilm(id);
    }

}
