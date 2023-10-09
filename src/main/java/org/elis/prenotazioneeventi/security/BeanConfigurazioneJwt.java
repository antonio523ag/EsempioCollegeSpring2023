package org.elis.prenotazioneeventi.security;

import org.elis.prenotazioneeventi.exception.UtenteNonTrovatoException;
import org.elis.prenotazioneeventi.repository.UtenteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfigurazioneJwt {

    private final UtenteRepository repo;

    public BeanConfigurazioneJwt(UtenteRepository repo) {
        this.repo = repo;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> repo.findByEmail(username).orElseThrow(UtenteNonTrovatoException::new);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider dap=new DaoAuthenticationProvider();
        dap.setPasswordEncoder(passwordEncoder());
        dap.setUserDetailsService(userDetailsService());
        return dap;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configure)
            throws Exception {
        return configure.getAuthenticationManager();
    }



}
