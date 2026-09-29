package com.example;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class KullaniciRepository implements PanacheRepository<Kullanici> {

    public Optional<Kullanici> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
}
