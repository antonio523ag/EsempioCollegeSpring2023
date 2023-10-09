package org.elis.prenotazioneeventi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.elis.prenotazioneeventi.model.Ruolo;
import org.elis.prenotazioneeventi.model.Utente;
import org.elis.prenotazioneeventi.service.definition.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class TokenUtil {

    @Autowired
    UtenteService service;

    private String key="miaChiaveCustom";

    private Key generaChiave(){
        return Keys.hmacShaKeyFor(key.getBytes());
    }

    private Claims creaPayloadToken(Utente u){
        String ruolo=u.getRuolo().toString();
        String username=u.getEmail();
        String dataNascita=u.getDataDiNascita().format(DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy"));
        String saluto="ciao, hai letto i miei dati, e adesso?";
        Claims claims= Jwts.claims().subject(username).build();
        claims.put("ruolo",ruolo);
        claims.put("dataNascita",dataNascita);
        claims.put("saluto",saluto);
        return claims;
    }
    public String generaToken(Utente u){
        //1000L (1sec)*60(1 min)*60(1h)*24(1g)*60(60g)
        long millisecondiDiDurata=1000L*60*60*24*60;
        Claims c=creaPayloadToken(u);
        String token=Jwts.builder()
                .setClaims(c)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+millisecondiDiDurata))
                .signWith(generaChiave())
                .compact();
        return token;
    }
    private Claims prendiClaimsDalToken(String token){
        JwtParser parser=Jwts.parser()
                .setSigningKey(generaChiave())
                .build();
        Claims claims=parser.parseClaimsJwt(token)
                .getPayload();

        return claims;
    }

    public String getSubject(String token){
        return prendiClaimsDalToken(token).getSubject();
    }

    public Utente getUtenteFromToken(String token){
        String username=getSubject(token);
        return service.findByEmail(username);
    }

    public LocalDate getDataNascita(String token){
        String data=prendiClaimsDalToken(token).get("dataNascita",String.class);
        return LocalDate.parse(data,DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy"));
    }

    public Ruolo getRuolo(String token){
        return prendiClaimsDalToken(token).get("ruolo",Ruolo.class);
    }






}
