package org.elis.prenotazioneeventi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class SingoloSpettacolo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    @OneToMany(mappedBy = "singoloSpettacolo")
    private List<Biglietto> posti;
    @ManyToOne
    @JoinColumn(name = "id_evento",nullable = false,updatable = false)
    private Evento evento;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Luogo luogo;
}
