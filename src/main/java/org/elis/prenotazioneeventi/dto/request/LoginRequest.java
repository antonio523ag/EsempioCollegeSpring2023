package org.elis.prenotazioneeventi.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
