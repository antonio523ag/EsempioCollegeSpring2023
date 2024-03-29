package org.elis.prenotazioneeventi.service.definition;

import org.elis.prenotazioneeventi.dto.request.FiltroUtente;
import org.elis.prenotazioneeventi.dto.request.LoginRequest;
import org.elis.prenotazioneeventi.dto.request.RegistrazioneRequest;
import org.elis.prenotazioneeventi.model.Utente;

import java.util.List;

public interface UtenteService {
    public Utente login(LoginRequest request);
    public boolean registrazione(RegistrazioneRequest request);

    public List<Utente> findAllClienti();
    public List<Utente> findAllVenditori();
    Utente findByEmail(String email);

    List<Utente> getUtentiFiltrati(FiltroUtente request);

    Utente login(String email, String password);
}
