package org.elis.prenotazioneeventi.repository;

import org.elis.prenotazioneeventi.model.Ruolo;
import org.elis.prenotazioneeventi.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente,Long> {

    @Query(nativeQuery = true,value = "select * from Utente where email = :giacomo and password = :password")
    Optional<Utente> loginConQueryNativa(String giacomo,String password);

    @Query("select u from Utente u where u.email = :email and u.password = :password")
    Optional<Utente> loginConJPQL(String email,String password);

    Optional<Utente> findByEmailAndPasswordAndBloccatoIsFalse(String email, String password);

    List<Utente> findAllByRuolo(Ruolo ruolo);
}
