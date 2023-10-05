package org.elis.prenotazioneeventi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Biglietto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String sezione;
    @Column(nullable = false)
    private String nomePosto;
    @Column(nullable = false)
    private double prezzo;
    @ManyToOne
    @JoinColumn(name = "id_spettacolo", nullable = false)
    private SingoloSpettacolo singoloSpettacolo;
    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente acquirente;

}
