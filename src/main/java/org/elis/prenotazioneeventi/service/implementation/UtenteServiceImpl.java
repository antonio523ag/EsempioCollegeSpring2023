package org.elis.prenotazioneeventi.service.implementation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.elis.prenotazioneeventi.dto.request.LoginRequest;
import org.elis.prenotazioneeventi.dto.request.RegistrazioneRequest;
import org.elis.prenotazioneeventi.exception.DatiNonValidiException;
import org.elis.prenotazioneeventi.exception.UtenteNonTrovatoException;
import org.elis.prenotazioneeventi.model.Ruolo;
import org.elis.prenotazioneeventi.model.Utente;
import org.elis.prenotazioneeventi.repository.UtenteRepository;
import org.elis.prenotazioneeventi.service.definition.UtenteService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UtenteServiceImpl implements UtenteService {


    //@Autowired
    private UtenteRepository utenteRepo;
    private final Validator validator;

    public UtenteServiceImpl(UtenteRepository repository, Validator validator){
        utenteRepo=repository;
        this.validator = validator;
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
        if(!errori.isEmpty()){
            throw new DatiNonValidiException(errori);
        }
        Optional<Utente> ut=utenteRepo.findByEmailAndPasswordAndBloccatoIsFalse(request.getEmail(),request.getPassword());
        /*if(ut.isEmpty()){
            throw new UtenteNonTrovatoException();
        }
        else return ut.get();*/
        return ut.orElseThrow(UtenteNonTrovatoException::new);
    }

    @Override
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
    public List<Utente> findAllClienti() {
        return utenteRepo.findAllByRuolo(Ruolo.CLIENTE);
    }

    @Override
    public List<Utente> findAllVenditori() {
        return utenteRepo.findAllByRuolo(Ruolo.VENDITORE);
    }

    @Override
    public Utente findByEmail(String email) {
        return utenteRepo.findByEmail(email).orElseThrow(UtenteNonTrovatoException::new);
    }
}
