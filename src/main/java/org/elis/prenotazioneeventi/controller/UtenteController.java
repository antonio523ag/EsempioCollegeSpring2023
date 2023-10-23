package org.elis.prenotazioneeventi.controller;

import org.elis.prenotazioneeventi.dto.request.FiltroUtente;
import org.elis.prenotazioneeventi.dto.request.LoginRequest;
import org.elis.prenotazioneeventi.dto.request.RegistrazioneRequest;
import org.elis.prenotazioneeventi.dto.response.LoginResponse;
import org.elis.prenotazioneeventi.dto.response.ClienteDTO;
import org.elis.prenotazioneeventi.model.Utente;
import org.elis.prenotazioneeventi.security.TokenUtil;
import org.elis.prenotazioneeventi.service.definition.UtenteService;
import org.elis.prenotazioneeventi.util.Utilities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
public class UtenteController {

    private final UtenteService service;
    private final TokenUtil util;

    public UtenteController(UtenteService service, TokenUtil util) {
        this.service = service;
        this.util = util;
    }

    @PostMapping("/all/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        Utente u=service.login(request.getEmail(),request.getPassword());
        //creo il token jwt
        String token=util.generaToken(u);
        LoginResponse l=new LoginResponse();
        l.setId(u.getId());
        l.setRuolo(u.getRuolo().name());
        l.setEmail(u.getEmail());
        l.setAnni((int) ChronoUnit.YEARS.between(u.getDataDiNascita(), LocalDate.now()));
        //prima del body lo setto come header e come chiave metto "Authorization"
        return ResponseEntity.status(HttpStatus.OK).header("Authorization",token).body(l);
    }

    @GetMapping("/admin/prova")
    public ResponseEntity<String> sonoUnAdmin(UsernamePasswordAuthenticationToken upat){
        if(upat==null)return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("non hai fatto la login");
        Utente u= (Utente) upat.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body("ciao sono "+u.getNome()+" "+u.getCognome());
    }

    @PostMapping("/all/registra")
    public ResponseEntity<Void> registrazione(@RequestBody RegistrazioneRequest request){
        boolean registrato= service.registrazione(request);
        if(registrato)return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }

    @GetMapping("/all/getAll")
    public ResponseEntity<List<ClienteDTO>> getAllClienti(){
        long tempoChiamata=System.currentTimeMillis();
        List<ClienteDTO> clienti=service.findAllClienti()
                .stream().map(Utilities::toClienteDTO).toList();
        tempoChiamata=System.currentTimeMillis()-tempoChiamata;
        System.out.println("tempo impiegato "+tempoChiamata);
        return ResponseEntity.status(HttpStatus.OK).body(clienti);
    }

    @GetMapping("/all/init")
    public ResponseEntity<Void> init(){
        for(int i=0;i<1000;i++){
            RegistrazioneRequest r=new RegistrazioneRequest();
            r.setNome("nome "+(i+1));
            r.setCognome("cognome "+(i+1));
            r.setCodiceFiscale("cod"+i);
            r.setPassword("Password!1");
            r.setPasswordRipetuta("Password!1");
            r.setEmail("n.c."+i+"@elis.org");
            r.setDataDiNascita(LocalDate.of(2000,1,1));
            service.registrazione(r);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/all/clientiFiltrati")
    public ResponseEntity<List<ClienteDTO>> getClientiFiltrati(@RequestBody FiltroUtente request){
        List<ClienteDTO> utenti=service.getUtentiFiltrati(request).stream().map(Utilities::toClienteDTO).toList();
        return ResponseEntity.status(HttpStatus.OK).body(utenti);
    }





}
