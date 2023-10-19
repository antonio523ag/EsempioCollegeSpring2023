package org.elis.prenotazioneeventi.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.elis.prenotazioneeventi.dto.request.FiltroAvanzo;
import org.elis.prenotazioneeventi.dto.request.FiltroUtente;
import org.elis.prenotazioneeventi.model.Categoria;
import org.elis.prenotazioneeventi.model.Evento;
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
            //con il cb.upper porto tutti gli elementi in UpperCase, in modo che, mettendo anche il parametro
            // in maiuscolo la mia ricerca diventa ignore case
            Predicate filtroNome = cb.like(cb.upper(root.get("nome")), "%" + request.getNome().toUpperCase() + "%");
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

    public List<Evento> getEventiFiltrati(FiltroAvanzo request){
        //creo il mio criteriaBulder passando dall'entityManager
        CriteriaBuilder builder=manager.getCriteriaBuilder();
        //sapendo già che la mia query ragionerà su piu classi quando creo
        //la criteriaQuery la creo su una wrapperClass che mi permetterà di contenerle
        //tutte, questa classe è la Tuple
        CriteriaQuery<Tuple> cq=builder.createQuery(Tuple.class);
        //dalla criteriaQuery vado a prendere quale sarà la classe "principale" sulla quale
        //far girare la mia query
        Root<Evento> root=cq.from(Evento.class);
        //sfruttando il mapping messo nelle entity vado a prendermi la classe relazionata
        //con un oggetto che al pari del root mi permetterà di accedere ai nomi dei campi
        Join<Evento, Categoria> join=root.join("categorie");
        //creo la lista di predicate dove inserirò tutte le mie condizioni
        List<Predicate> predicate=new ArrayList<>();
        //faccio scorrere tutte le chiavi della map filtroEvento, le chiavi saranno i nomi
        //dei campi presi in consideranzione per il nostro filtro
        for(String nomeCampo:request.getFiltroEvento().keySet()){
            //prendo il valore che devo settare nel filtro di quel determinato campo
            String valoreCampo=request.getFiltroEvento().get(nomeCampo);
            //e creo il mio predicate
            Predicate p=builder.like(root.get(nomeCampo),("%"+valoreCampo+"%"));
            //lo aggiungo alla lista
            predicate.add(p);
        }
        //ripeto le stesse operazioni anche per la map filtroCategoria
        for(String s:request.getFiltroCategoria().keySet()){
            Predicate p=builder.like(join.get(s),"%"+request.getFiltroCategoria().get(s)+"%");
            predicate.add(p);
        }
        //una volta creati tutti i predicate vado a dire alla CriteriaQuery che la selezione
        //non verrà fatta solo su una tabella (non esiste la tabella Tuple)
        //ma su quali tabelle farla (multiselect(root,join)
        //e gli setto tutte le condizioni che ho creato
        cq.multiselect(root,join).where(predicate.toArray(new Predicate[predicate.size()]));
        //quando eseguo la mia query vado a prendere non una lista di Eventi ma una lista di
        //tuple che avrà per ogni colonna un valore divero messi in ordine di come li ho passati
        //al metodo multiselect (colonna 0 ->root->Evento, Colonna 1 ->join->Categoria)
        List<Tuple> tuple=manager.createQuery(cq).getResultList();
        //tramite uno stream vado a riprendermi tutti gli elementi nella colonna che mi interessa
        //(mi interessano gli eventi quindi colonna 0), il metodo get della classe Tuple
        //torna un generics, quindi vuole sapere il tipo a cui Castare il nostro oggetto
        //Evento.class
        return tuple.stream().map(t->t.get(0,Evento.class)).toList();

    }

}
