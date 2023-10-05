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
public class Luogo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String via;
    private String citta;
    private String provincia;
    private String cap;
    @OneToMany(mappedBy = "luogo")
    private List<Sezione> sezioni;
    @OneToMany(mappedBy = "luogo")
    private List<SingoloSpettacolo> spettacoliRealizzatiInQuestoLuogo;
}
