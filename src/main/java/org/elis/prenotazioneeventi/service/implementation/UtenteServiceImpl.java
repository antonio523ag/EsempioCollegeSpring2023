package org.elis.prenotazioneeventi.service.implementation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.elis.prenotazioneeventi.dto.request.FiltroUtente;
import org.elis.prenotazioneeventi.dto.request.LoginRequest;
import org.elis.prenotazioneeventi.dto.request.RegistrazioneRequest;
import org.elis.prenotazioneeventi.exception.DatiNonValidiException;
import org.elis.prenotazioneeventi.exception.UtenteNonTrovatoException;
import org.elis.prenotazioneeventi.model.Ruolo;
import org.elis.prenotazioneeventi.model.Utente;
import org.elis.prenotazioneeventi.repository.CriteriaUtenteRepository;
import org.elis.prenotazioneeventi.repository.UtenteRepository;
import org.elis.prenotazioneeventi.service.definition.UtenteService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@CacheConfig(cacheNames = {"utenti"})
public class UtenteServiceImpl implements UtenteService {


    //@Autowired
    private UtenteRepository utenteRepo;
    private final Validator validator;

    private final CriteriaUtenteRepository criteriaUtenteRepository;

    public UtenteServiceImpl(UtenteRepository repository, Validator validator, CriteriaUtenteRepository criteriaUtenteRepository){
        utenteRepo=repository;
        this.validator = validator;
        this.criteriaUtenteRepository = criteriaUtenteRepository;
    }

    @Override
    public Utente login(LoginRequest request) {
        Map<String,String> errori=new TreeMap<>();
        if(request==null) {
            errori.put("request","la request non può essere null");
            throw new DatiNonValidiException(errori);
        }
        if(request.getEmail()==null||request.getEmail().isEmpty()){
            errori.put("email","l'email non può essere vuota");
        }
        if(request.getPassword()==null||request.getPassword().isEmpty()){
            errori.put("password","la password non può essere vuota");
        }
        if(!request.getEmail().matches("^(.+)@(\\S+)$")){
            errori.put("email","l'email non rispetta il formato standard");
        }
        if(!errori.isEmpty()){
            throw new DatiNonValidiException(errori);
        }
        return login(request.getEmail(),request.getPassword());
    }

    @Cacheable(value = "utente",key = "#email.concat('-').concat(#password)")
    public Utente login(String email,String password){
        Optional<Utente> ut=utenteRepo.findByEmailAndPasswordAndBloccatoIsFalse(email,password);
        /*if(ut.isEmpty()){
            throw new UtenteNonTrovatoException();
        }
        else return ut.get();*/
        return ut.orElseThrow(UtenteNonTrovatoException::new);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "utenti"),
            @CacheEvict(cacheNames = "venditori"),
            @CacheEvict(cacheNames = "clienti"),
            //non avendo un parametro in ingresso che si chiama email non posso inserire
            //direttamente #email, ci accederò tramite la variabile request, facendo
            //nomeOggetto.nomeParametro
            @CacheEvict(cacheNames = "utente", key = "#request.email")
    })
    public boolean registrazione(RegistrazioneRequest request) {
        Set<ConstraintViolation<RegistrazioneRequest>> violation=validator.validate(request);
        if(!violation.isEmpty()){
            if(violation.size()>1)throw new ConstraintViolationException(violation);
            for(ConstraintViolation<RegistrazioneRequest> r:violation){
                if(!r.getPropertyPath().toString().equals("idRichiedente")){
                   throw new ConstraintViolationException(violation);
                }
            }
        }
        Utente u=new Utente();
        u.setEmail(request.getEmail());
        u.setCognome(request.getCognome());
        u.setNome(request.getNome());
        u.setDataDiNascita(request.getDataDiNascita());
        u.setCodiceFiscale(request.getCodiceFiscale());
        u.setPassword(request.getPassword());
        //se nella request non sono state settate email richiedente e password richiedente
        //vuol dire che è l'utente stesso che vuole registrarsi
        //quindi setto il ruolo a cliente
        if(request.getIdRichiedente()<=0){
            u.setRuolo(Ruolo.CLIENTE);
        }else{

            //se email e password sono presenti allora controllo se l'utente che fa la richiesta
            //di creazione di un account è presente sul database
            Optional<Utente> ou=utenteRepo.findById(request.getIdRichiedente());
            //se quell'utente non è presente o è bloccato non gli prermetto di fare registrazioni
            if(ou.isEmpty()) return false;
            //sono sicuro che l'utente sia presente sul db quindi lo prendo
            Utente richiedente=ou.get();
            //se è un venditore, è l'unica
            if(richiedente.getRuolo().equals(Ruolo.VENDITORE)||richiedente.getRuolo().equals(Ruolo.CLIENTE)) return false;

        }
        System.out.println("registro l'utente");
        u=utenteRepo.save(u);
        return true;
    }

    @Override
    @Cacheable(value = "clienti")
    public List<Utente> findAllClienti() {
        return utenteRepo.findAllByRuolo(Ruolo.CLIENTE);
    }

    @Override
    @Cacheable(value = "venditori")
    public List<Utente> findAllVenditori() {
        return utenteRepo.findAllByRuolo(Ruolo.VENDITORE);
    }

    @Cacheable(value = "utente", key = "#email", sync = true)
    @Override
    public Utente findByEmail(String email) {
        return utenteRepo.findByEmail(email).orElseThrow(UtenteNonTrovatoException::new);
    }

    @Override
    public List<Utente> getUtentiFiltrati(FiltroUtente request) {
        return criteriaUtenteRepository.getUtentiFiltrati(request);
    }
}
