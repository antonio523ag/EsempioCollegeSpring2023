package org.elis.prenotazioneeventi.dto.request;

import lombok.Data;

import java.util.Map;

@Data
public class FiltroAvanzo {
    private Map<String,String> filtroEvento;
    private Map<String,String> filtroCategoria;
}
