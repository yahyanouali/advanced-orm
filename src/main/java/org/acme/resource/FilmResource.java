package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.acme.entities.Film;
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
    public List<Film> getFilms() {
        var films = filmRepository.getFilms();
        log.info("Films trouvés : {}", films);
        return films;
    }

    @GET
    @Path("/{id}")
    public Film getFilmById(@PathParam("id") Integer id) {
        var film = filmRepository.getFilmById(id);
        log.info("Film trouvé : {}", film);
        return film;
    }

    @POST
    public void createFilm(Film film) {
        log.info("Création du film : {}", film);
        filmRepository.saveFilm(film);
    }

    @PUT
    public void updateFilm(Film film) {
        log.info("Mise à jour du film : {}", film);
        filmRepository.updateFilm(film);
    }

    @DELETE
    @Path("/{id}")
    public void deleteFilm(@PathParam("id") Integer id) {
        log.info("Suppression du film avec l'id : {}", id);
        filmRepository.deleteFilm(id);
    }

}
