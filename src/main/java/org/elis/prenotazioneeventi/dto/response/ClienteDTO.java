package org.elis.prenotazioneeventi.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteDTO {
    private long id;
    private String nome;
    private String cognome;
    private String email;
    private LocalDate dataDiNascita;
    private String codiceFiscale;
}
