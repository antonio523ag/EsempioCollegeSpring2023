package org.elis.prenotazioneeventi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nome;
    private String descrizione;
    @ManyToMany(mappedBy = "eventi")
    private List<Categoria> categorie;
    @OneToMany(mappedBy = "evento")
    private List<SingoloSpettacolo> ripetizioni;
    @ManyToOne
    @JoinColumn(nullable = false,updatable = false,name = "id_creatore")
    private Utente utente;

}
