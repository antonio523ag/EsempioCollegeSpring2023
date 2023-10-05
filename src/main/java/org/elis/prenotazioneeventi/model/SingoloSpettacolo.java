package org.elis.prenotazioneeventi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Ripetizione {
    private long id;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private List<Biglietto> posti;
}
