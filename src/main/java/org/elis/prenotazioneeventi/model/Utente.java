package org.elis.prenotazioneeventi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "custom_utente")
public class Utente implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String cognome;
    @Column(unique = true,nullable = false,name = "username")
    private String email;
    @Column(nullable = false)
    private String password;
    private LocalDate dataDiNascita;
    private String codiceFiscale;
    @Column(nullable = false)
    private Ruolo ruolo;
    @OneToMany(mappedBy = "acquirente")
    private List<Biglietto> biglietti;
    @OneToMany(mappedBy = "utente")
    private List<Evento> eventiOrganizzati;
    @Column(nullable = false)
    private boolean bloccato;

    public Utente(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }
    //da qui iniziano i metodi dello UserDetails

    //questo è il metodo che ci permetterà di automatizzare l'accesso dei vari ruoli sui servizi scritti
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //il testo passato al costruttore del SimpleGrantedAuthority vuole SEMPRE la scritta ROLE_ prima del nome del ruolo
        SimpleGrantedAuthority sga=new SimpleGrantedAuthority("ROLE_"+ruolo);
        List<SimpleGrantedAuthority> list=List.of(sga);
        return list;
    }
    //metodo per visualizzare lo username dell'utente (nel nostro caso utilizziamo l'email come username)
    @Override
    public String getUsername() {
        return email;
    }

    //metodo utilizzato (di solito) per controllare se l'account non è "scaduto"
    //possibili implementazioni possono essere per account temporarei oppure per un controllo
    //della login (se non fai la login da almeno 6 mesi "disattivo" l'account
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //metodo utilizzato per controllare che l'account sia ancora attivo e che non sia stato
    //bloccato da un admin
    @Override
    public boolean isAccountNonLocked() {
        return !bloccato;
    }
    //alcuni applicativi hanno l'obbligo di rinnovare la password ogni tot tempo oppure
    //gli account durano un periodo prestabilito e non saranno rinnovabili
    //metodo per controllare che sia ancora tutto valido
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //metodo per controllare se l'account è stato attivato (utilizzato soprattutto dai
    //siti la cui registrazione deve essere approvata)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
