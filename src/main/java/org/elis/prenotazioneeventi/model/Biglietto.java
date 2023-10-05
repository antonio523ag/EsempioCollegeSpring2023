package org.elis.prenotazioneeventi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PostoDisponibile {
    private long id;
    private String sezione;
    private String nomePosto;
    private double prezzo;
    private Ripetizione ripetizione;
    private Utente acquirente;

}
