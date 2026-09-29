package com.example;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Kullanici {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String isim;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(nullable = false, unique = true)
    private String email;

    // Getter ve Setter'lar
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsim() { return isim; }
    public void setIsim(String isim) { this.isim = isim; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
