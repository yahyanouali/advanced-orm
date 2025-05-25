package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.acme.entities.Artiste;
import org.acme.service.ArtisteService;

import java.util.List;

@Slf4j
@Path("/artists")
public class ArtisteResource {
    @Inject
    ArtisteService artisteService;

    @GET
    @Path("/")
    public List<Artiste> getArtists() {
        var artistes = artisteService.getArtistes();
        log.info("Artistes trouvés : {}", artistes);
        return artistes;
    }

    @GET
    @Path("/{id}")
    public Artiste getArtistById(@PathParam("id") Integer id) {
        var artiste = artisteService.getArtisteById(id);
        log.info("Artiste trouvé : {}", artiste);
        return artiste.orElse(null);
    }

}
