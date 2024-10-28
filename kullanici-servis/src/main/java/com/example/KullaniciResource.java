package com.example;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/kullanici")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KullaniciResource {

    @Inject
    KullaniciRepository kullaniciRepository;

    @GET
    public List<Kullanici> getAllKullanicilar() {
        return kullaniciRepository.listAll();
    }

    @POST
    @Transactional
    public Kullanici createKullanici(Kullanici kullanici) {
        kullaniciRepository.persist(kullanici);
        return kullanici;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Kullanici updateKullanici(@PathParam("id") Long id, Kullanici kullanici) {
        Kullanici existing = kullaniciRepository.findById(id);
        if (existing == null) {
            throw new WebApplicationException("Kullanıcı bulunamadı", 404);
        }
        existing.setIsim(kullanici.getIsim());
        existing.setEmail(kullanici.getEmail());
        return existing;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteKullanici(@PathParam("id") Long id) {
        Kullanici kullanici = kullaniciRepository.findById(id);
        if (kullanici == null) {
            throw new WebApplicationException("Kullanıcı bulunamadı", 404);
        }
        kullaniciRepository.delete(kullanici);
    }
}
