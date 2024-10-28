package com.example;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Kullanici {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String isim;
    private String email;

    // Getter ve Setter'lar
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsim() { return isim; }
    public void setIsim(String isim) { this.isim = isim; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
