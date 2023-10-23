package org.elis.prenotazioneeventi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//serve ad abilitare la cache su tutto il progetto
@EnableCaching
public class PrenotazioneEventiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrenotazioneEventiApplication.class, args);
    }

}
