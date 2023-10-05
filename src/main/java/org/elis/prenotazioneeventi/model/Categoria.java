package org.elis.prenotazioneeventi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false,updatable = false,unique = true)
    private String nome;
    @ManyToMany
    @JoinTable(name = "eventi_categorie",
            joinColumns = @JoinColumn(name = "id_categoria",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "id_evento",nullable = false))
    private List<Evento> eventi;
}
