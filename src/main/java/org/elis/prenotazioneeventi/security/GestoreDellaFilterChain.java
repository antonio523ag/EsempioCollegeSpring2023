package org.elis.prenotazioneeventi.security;

import org.elis.prenotazioneeventi.model.Ruolo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class GestoreDellaFilterChain {

    private final FilterJwt filterJwt;
    private final AuthenticationProvider provider;

    public GestoreDellaFilterChain(FilterJwt filterJwt, AuthenticationProvider provider) {
        this.filterJwt = filterJwt;
        this.provider = provider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .headers(cust->cust.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/modificaprezzobiglietto").hasRole(Ruolo.VENDITORE.toString())
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/all/**").permitAll()
                        .requestMatchers("/client/**").hasRole(Ruolo.CLIENTE.toString())
                        .requestMatchers("/seller/**").hasRole(Ruolo.VENDITORE.toString())
                        .requestMatchers("/admin/**").hasAnyRole(Ruolo.ADMIN.toString(),Ruolo.SUPERADMIN.toString())
                        .anyRequest().permitAll()
                ).sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(AbstractHttpConfigurer::disable)
                .authenticationProvider(provider)
                .addFilterBefore(filterJwt, UsernamePasswordAuthenticationFilter.class);
                return httpSecurity.build();
    }
}
