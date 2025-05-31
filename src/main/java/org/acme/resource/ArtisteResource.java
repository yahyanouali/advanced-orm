package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.acme.entities.ArtisteEntity;
import org.acme.service.ArtisteRepository;

import java.util.List;

@Slf4j
@Path("/artists")
public class ArtisteResource {
    @Inject
    ArtisteRepository artisteRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ArtisteEntity> getAllArtists() {
        return artisteRepository.getArtistes();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArtistById(@PathParam("id") Integer id) {
        return Response.ok(artisteRepository.getArtisteById(id)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createArtist(ArtisteEntity artisteEntity) {
        artisteRepository.saveArtiste(artisteEntity);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateArtist(@PathParam("id") Integer id, ArtisteEntity artisteEntity) {
        artisteEntity.setId(id);
        artisteRepository.updateArtiste(artisteEntity);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteArtist(@PathParam("id") Integer id) {
        artisteRepository.deleteArtiste(id);
        return Response.noContent().build();
    }

}
