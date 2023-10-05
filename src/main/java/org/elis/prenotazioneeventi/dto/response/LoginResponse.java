package org.elis.prenotazioneeventi.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private long id;
    private String email;
    private String ruolo;
    private int anni;

}
