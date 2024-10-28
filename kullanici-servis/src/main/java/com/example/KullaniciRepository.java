package com.example;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KullaniciRepository implements PanacheRepository<Kullanici> {
}
