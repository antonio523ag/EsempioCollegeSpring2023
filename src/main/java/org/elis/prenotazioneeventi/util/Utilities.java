package org.elis.prenotazioneeventi.util;

import org.elis.prenotazioneeventi.dto.response.ClienteDTO;
import org.elis.prenotazioneeventi.model.Utente;

public class Utilities {
    public static ClienteDTO toClienteDTO(Utente u){
        ClienteDTO c=new ClienteDTO();
        c.setNome(u.getNome());
        c.setCognome(u.getCognome());
        c.setId(u.getId());
        c.setEmail(u.getEmail());
        c.setCodiceFiscale(u.getCodiceFiscale());
        c.setDataDiNascita(u.getDataDiNascita());
        return c;
    }
}
