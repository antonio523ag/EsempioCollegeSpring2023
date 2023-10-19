package org.elis.prenotazioneeventi.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.elis.prenotazioneeventi.dto.request.FiltroUtente;
import org.elis.prenotazioneeventi.model.Utente;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CriteriaUtenteRepository {
    private final EntityManager manager;

    public CriteriaUtenteRepository(EntityManager manager) {
        this.manager = manager;
    }
/*
    public List<Utente> getUtentiFiltrati(FiltroUtente request){
        //la prima cosa di cui abbiamo bisogno è il costruttore delle CriteriaQuery
        //lo prenderò dall'EntityManager
        CriteriaBuilder cb=manager.getCriteriaBuilder();
        //una volta creato il costruttore vado a creare la mia CriteriaQuery
        CriteriaQuery<Utente> cq= cb.createQuery(Utente.class);
        //creo l'oggetto che mi permetterò di accedere ai nomi dei campi della classe utente
        Root<Utente> root=cq.from(Utente.class);
        //vado a creare le condizioni per cui il mio filtro deve funzionare
        Predicate filtroNome=cb.like(root.get("nome"),"%"+request.getNome()+"%");
        Predicate filtroCognome=cb.like(root.get("cognome"),"%"+request.getCognome()+"%");
        Predicate filtroEmail=cb.like(root.get("email"),"%"+request.getEmail()+"%");
        Predicate filtroCodFisc=cb.equal(root.get("codiceFiscale"),request.getCodiceFiscale());
        //setto i filtri alla criteria query
        cq.where(filtroNome,filtroCognome,filtroEmail,filtroCodFisc);
        //creo la query finale che sarà una query tipizzata
        TypedQuery<Utente> tq=manager.createQuery(cq);
        //prendo la lista dei risultati
        List<Utente> utenti=tq.getResultList();
        //ritorno i risultati;
        return utenti;
    }

    */
public List<Utente> getUtentiFiltrati(FiltroUtente request){
    //la prima cosa di cui abbiamo bisogno è il costruttore delle CriteriaQuery
    //lo prenderò dall'EntityManager
    CriteriaBuilder cb=manager.getCriteriaBuilder();
    //una volta creato il costruttore vado a creare la mia CriteriaQuery
    CriteriaQuery<Utente> cq= cb.createQuery(Utente.class);
    //creo l'oggetto che mi permetterò di accedere ai nomi dei campi della classe utente
    Root<Utente> root=cq.from(Utente.class);
    //Creo la lista di predicate dove salverò tutti i miei filtri
    List<Predicate> predicate=new ArrayList<>();
    //vado a creare le condizioni per cui il mio filtro deve funzionare
    if(request.getNome()!=null&&!request.getNome().isEmpty()) {
        Predicate filtroNome = cb.like(root.get("nome"), "%" + request.getNome() + "%");
        predicate.add(filtroNome);
    }
    if(request.getCognome()!=null&&!request.getCognome().isEmpty()) {
        Predicate filtroCognome = cb.like(root.get("cognome"), "%" + request.getCognome() + "%");
        predicate.add(filtroCognome);
    }
    if(request.getEmail()!=null&&!request.getEmail().isEmpty()) {
        Predicate filtroEmail = cb.like(root.get("email"), "%" + request.getEmail() + "%");
        predicate.add(filtroEmail);
    }
    if(request.getCodiceFiscale()!=null&&!request.getCodiceFiscale().isEmpty()){
        Predicate filtroCodFisc=cb.equal(root.get("codiceFiscale"),request.getCodiceFiscale());
        predicate.add(filtroCodFisc);
    }
    //converto la lista in array per poterla settare alla criteriaQuery
    Predicate[] arrayDiPredicate=predicate.toArray(new Predicate[predicate.size()]);
    //setto i filtri alla criteria query
    cq.where(arrayDiPredicate);
    //creo la query finale che sarà una query tipizzata
    TypedQuery<Utente> tq=manager.createQuery(cq);
    //prendo la lista dei risultati
    List<Utente> utenti=tq.getResultList();
    //ritorno i risultati;
    return utenti;
}
}
