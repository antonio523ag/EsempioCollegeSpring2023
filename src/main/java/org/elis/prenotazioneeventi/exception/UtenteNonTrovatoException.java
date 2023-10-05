package org.elis.prenotazioneeventi.exception;

public class UtenteNonTrovatoException  extends RuntimeException{

    public UtenteNonTrovatoException() {
        super("nessun utente attivo trovato con questa username e/o password");
    }
}
