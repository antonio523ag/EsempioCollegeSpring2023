package org.elis.prenotazioneeventi;

import org.elis.prenotazioneeventi.model.Ruolo;
import org.elis.prenotazioneeventi.model.Utente;
import org.elis.prenotazioneeventi.repository.UtenteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class TestRepository {
    @Autowired
    private UtenteRepository repo;

    @Test
    public void testDimensioneElencoDati(){
        List<Utente> utenti=repo.findAllByRuolo(Ruolo.VENDITORE);
        assertEquals(1,utenti.size());
    }

    @Test
    public void testLogin(){
        assertThat(repo.findByEmailAndPasswordAndBloccatoIsFalse("a.grillo@elis.org","Password!1"))
                .get().extracting(Utente::getNome)
                .isEqualTo("Antonio");
    }
}
