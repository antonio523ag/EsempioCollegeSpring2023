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
public class Sezione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nome;
    @OneToMany(mappedBy = "sezione")
    private List<Posto> posti;
    @ManyToOne
    @JoinColumn(name = "id_luogo",nullable = false)
    private Luogo luogo;
}
