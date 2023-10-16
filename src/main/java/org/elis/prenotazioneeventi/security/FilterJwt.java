package org.elis.prenotazioneeventi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.elis.prenotazioneeventi.model.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FilterJwt extends OncePerRequestFilter {

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authCode=request.getHeader("Authorization");
        System.out.println("chiamata");
        if(authCode!=null&&authCode.startsWith("Bearer")){
            String token=authCode.substring(7);//"Bearer " viene tolto
            Utente u=tokenUtil.getUtenteFromToken(token);
            UsernamePasswordAuthenticationToken upat=
                    new UsernamePasswordAuthenticationToken(u,null,u.getAuthorities());
            upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(upat);
        }
        filterChain.doFilter(request,response);
    }
}
