package org.elis.prenotazioneeventi.controller;

import org.elis.prenotazioneeventi.dto.request.AggiungiLuogoRequest;
import org.elis.prenotazioneeventi.model.Luogo;
import org.elis.prenotazioneeventi.model.Posto;
import org.elis.prenotazioneeventi.model.Sezione;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LuogoController {
/*    public ResponseEntity<Void> aggiungiLuogo(@RequestBody AggiungiLuogoRequest request){
        Luogo l=new Luogo();
        l.setCap("sdfs");
        List<Sezione> sezioni=request.getSezioni().stream()
                .map(s->{
                    Sezione nuova=new Sezione();
                    nuova.setNome(s.getNome());
                    nuova.setLuogo(l);
                    nuova.setPosti(s.getNomiPosti().stream().map(p->{
                        Posto nuovo=new Posto();
                        nuovo.setNome(p);
                        nuovo.setSezione(nuova);
                        return nuovo;
                    }).toList());
                    return nuova;
                }).toList();
        l.setSezioni(sezioni);
    }*/
}
