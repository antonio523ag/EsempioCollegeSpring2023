package org.elis.prenotazioneeventi.repository;

import org.elis.prenotazioneeventi.model.Biglietto;
import org.elis.prenotazioneeventi.model.SingoloSpettacolo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BigliettoRepository extends JpaRepository<Biglietto,Long> {

    List<Biglietto> findAllBySingoloSpettacoloAndAcquirenteIsNull(SingoloSpettacolo s);

    List<Biglietto> findAllBySingoloSpettacolo_IdAndAcquirenteIsNotNull(long id);
}
