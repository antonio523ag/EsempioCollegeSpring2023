package org.elis.prenotazioneeventi.exception;

import java.util.Map;

public class DatiNonValidiException extends RuntimeException{
    Map<String,String> errori;

    public DatiNonValidiException(Map<String,String> errori){
        this.errori=errori;
    }

    public Map<String, String> getErrori() {
        return errori;
    }
}
