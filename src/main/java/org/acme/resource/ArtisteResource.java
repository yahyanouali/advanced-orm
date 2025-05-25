package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.acme.entities.Artiste;
import org.acme.service.ArtisteService;

import java.util.List;

@Slf4j
@Path("")
public class ArtisteResource {
    @Inject
    ArtisteService artisteService;

    @GET
    @Path("/artists")
    public List<Artiste> getArtists() {
        var artistes = artisteService.getArtistes();
        log.info("Artistes trouvés : {}", artistes);
        return artistes;
    }

}
