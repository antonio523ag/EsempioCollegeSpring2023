package org.elis.prenotazioneeventi.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.elis.prenotazioneeventi.dto.request.LoginRequest;
import org.elis.prenotazioneeventi.dto.request.RegistrazioneRequest;
import org.elis.prenotazioneeventi.dto.response.LoginResponse;
import org.elis.prenotazioneeventi.model.Utente;
import org.elis.prenotazioneeventi.service.definition.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
public class UtenteController {

    @Autowired
    UtenteService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        Utente u=service.login(request);
        LoginResponse l=new LoginResponse();
        l.setId(u.getId());
        l.setRuolo(u.getRuolo().name());
        l.setEmail(u.getEmail());
        l.setAnni((int) ChronoUnit.YEARS.between(u.getDataDiNascita(), LocalDate.now()));
        return ResponseEntity.status(HttpStatus.OK).body(l);
    }

    @PostMapping("/registra")
    public ResponseEntity<Void> registrazione(@RequestBody RegistrazioneRequest request){
        boolean registrato= service.registrazione(request);
        if(registrato)return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }
//
//    public ResponseEntity<List<UtenteDTO>> getAllClienti(){
//        List<Utente> utenti=service.findAllClienti();
//
//    }

}
