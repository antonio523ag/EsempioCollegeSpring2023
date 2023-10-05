package org.elis.prenotazioneeventi.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AggiungiLuogoRequest {
    private String nome;
    private String via;
    private String citta;
    private String provincia;
    private String cap;
    private List<AggiungiSezioneRequest> sezioni;
}
