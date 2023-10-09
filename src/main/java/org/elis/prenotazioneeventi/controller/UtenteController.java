package org.elis.prenotazioneeventi.controller;

import org.elis.prenotazioneeventi.dto.request.LoginRequest;
import org.elis.prenotazioneeventi.dto.request.RegistrazioneRequest;
import org.elis.prenotazioneeventi.dto.response.LoginResponse;
import org.elis.prenotazioneeventi.dto.response.ClienteDTO;
import org.elis.prenotazioneeventi.model.Utente;
import org.elis.prenotazioneeventi.security.TokenUtil;
import org.elis.prenotazioneeventi.service.definition.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        Utente u=service.login(request);
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
    public ResponseEntity<String> sonoUnAdmin(){
        return ResponseEntity.status(HttpStatus.OK).body("ciao sono un admin");
    }

    @PostMapping("/registra")
    public ResponseEntity<Void> registrazione(@RequestBody RegistrazioneRequest request){
        boolean registrato= service.registrazione(request);
        if(registrato)return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }

    @GetMapping("/all/getAll")
    public ResponseEntity<List<ClienteDTO>> getAllClienti(){
        List<ClienteDTO> clienti=service.findAllClienti()
                .stream().map(this::toClienteDTO).toList();
        return ResponseEntity.status(HttpStatus.OK).body(clienti);


    }

    private ClienteDTO toClienteDTO(Utente u){
        ClienteDTO c=new ClienteDTO();
        c.setNome(u.getNome());
        c.setCognome(u.getCognome());
        c.setId(u.getId());
        c.setEmail(u.getEmail());
        c.setCodiceFiscale(u.getCodiceFiscale());
        c.setDataDiNascita(u.getDataDiNascita());
        return c;
    }



}
