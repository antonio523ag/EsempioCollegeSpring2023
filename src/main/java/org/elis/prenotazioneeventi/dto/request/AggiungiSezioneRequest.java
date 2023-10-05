package org.elis.prenotazioneeventi.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class AggiungiSezioneRequest {

    private String nome;
    private List<String> nomiPosti;
}
