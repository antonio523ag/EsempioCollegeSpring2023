package org.elis.prenotazioneeventi.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrazioneRequest {
    @NotBlank(message = "il nome non può essere null o vuoto")
    private String nome;
    @NotBlank(message = "il cognome non può essere null o vuoto")
    private String cognome;
    @Email(message = "quella che hai inserito non è una email")
    private String email;
    @NotBlank(message = "la password non può essere null o vuota")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", message = "inserisci un carattere maiuscolo, una minuscola, un carattere speciale e un numero, tra gli 8 e i 20 caratteri")
    private String password;
    @NotBlank(message = "la password ripetuta non può essere null o vuota")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", message = "inserisci un carattere maiuscolo, una minuscola, un carattere speciale e un numero, tra gli 8 e i 20 caratteri")
    private String passwordRipetuta;
    @Past(message = "non puoi essere nato nel futuro")
    private LocalDate dataDiNascita;
    @Size(min = 16,max = 16, message = "il codice fiscale sono 16 caratteri")
    private String codiceFiscale;
    @Min(value = 1, message = "non esistono id minori di 1")
    private long idRichiedente;
}
