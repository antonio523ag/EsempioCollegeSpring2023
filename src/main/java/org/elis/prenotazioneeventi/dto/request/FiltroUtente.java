package org.elis.prenotazioneeventi.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class FiltroUtente {

    //questa map sarà chiave valore dove la chiave è il nome del campo
    //e il valore sarà il valore da cercare
    //private Map<String,String> filtri;

    private String nome,cognome,email,codiceFiscale;
}
