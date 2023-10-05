package org.elis.prenotazioneeventi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String cognome;
    @Column(unique = true,nullable = false,name = "username")
    private String email;
    @Column(nullable = false)
    private String password;
    private LocalDate dataDiNascita;
    private String codiceFiscale;
    @Column(nullable = false)
    private Ruolo ruolo;
    @OneToMany(mappedBy = "acquirente")
    private List<Biglietto> biglietti;
    @OneToMany(mappedBy = "utente")
    private List<Evento> eventiOrganizzati;
    @Column(nullable = false)
    private boolean bloccato;

    public Utente(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }
}
