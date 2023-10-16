package org.elis.prenotazioneeventi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elis.prenotazioneeventi.dto.request.LoginRequest;
import org.elis.prenotazioneeventi.dto.request.RegistrazioneRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

@SpringBootTest
@ContextConfiguration(classes = PrenotazioneEventiApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc(print = MockMvcPrint.LOG_DEBUG,printOnlyOnFailure = false)
class PrenotazioneEventiApplicationTests {

    @Autowired
    private MockMvc mock;


    @Order(2)
    @Test
    public void testLogin1() throws Exception {
        mock.perform(MockMvcRequestBuilders.post("/all/login")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Order(3)
    @Test
    public void testLogin2() throws Exception {
        //creo l'oggetto della request
        LoginRequest l=new LoginRequest();
        l.setEmail("g.bongiovanni");
        l.setPassword("Password!1");
        //lo converto in json
        String json=new ObjectMapper().writeValueAsString(l);
        //creo la mia request
        MockHttpServletRequestBuilder builder=MockMvcRequestBuilders.post("/all/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        //la passo al mock
        mock.perform(builder)
                //controllo il risultato
            .andExpect(MockMvcResultMatchers.status().is(400));
    }


    @Order(1)
    @Test
    public void testRegistrazione() throws Exception {
        RegistrazioneRequest request=new RegistrazioneRequest();
        request.setNome("Giacomo");
        request.setCognome("Bongiovanni");
        request.setPassword("Password1!");
        request.setEmail("g.bongiovanni@elis.org");
        request.setCodiceFiscale("GCMBGN12D34E546T");
        request.setDataDiNascita(LocalDate.of(2003,5,19));
        request.setPasswordRipetuta("Password1!");
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json=objectMapper.writeValueAsString(request);
        //creo la mia request
        MockHttpServletRequestBuilder builder=MockMvcRequestBuilders.post("/all/registra")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        mock.perform(builder).andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    @WithUserDetails("a.grillo@elis.org")
    public void provaSecurity() throws Exception {
        mock.perform(MockMvcRequestBuilders.get("/admin/prova"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
    }

    @Test
    public void provaSecurityConToken() throws Exception {
        LoginRequest request=new LoginRequest();
        request.setEmail("a.grillo@elis.org");
        request.setPassword("Password!1");
        String json=new ObjectMapper().writeValueAsString(request);
        String tokenJwt=mock.perform(MockMvcRequestBuilders.post("/all/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andReturn().getResponse().getHeader("Authorization");
        //String tokenJwt="eyJhbGciOiJIUzUxMiJ9.eyJydW9sbyI6IkFETUlOIiwiZGF0YU5hc2NpdGEiOiJnaW92ZWTDrCAwNyBkaWNlbWJyZSAxOTg5Iiwic2FsdXRvIjoiY2lhbywgaGFpIGxldHRvIGkgbWllaSBkYXRpLCBlIGFkZXNzbz8iLCJzdWIiOiJhLmdyaWxsb0BlbGlzLm9yZyIsImlhdCI6MTY5NzQ1MTE3NCwiZXhwIjoxNzAyNjM1MTc0fQ.Hy_IAkIQ9K8yaVyOSa36Ust1KIpQmCYOp_w4Rl9YDnhC-HQQDK62gmfGS-RbPltVFP_ATgDlJuF1EN0Nn70RXg";
        System.out.println(tokenJwt);
        mock.perform(MockMvcRequestBuilders.get("/admin/prova")
                        .header("Authorization","Bearer "+tokenJwt)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
    }



    @Test
    public void testResultValueOnLogin() throws Exception{
        LoginRequest request=new LoginRequest();
        request.setEmail("a.grillo@elis.org");
        request.setPassword("Password!1");
        String json=new ObjectMapper().writeValueAsString(request);
        String tokenJwt=mock.perform(MockMvcRequestBuilders.post("/all/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                        .exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                        .value("a.grillo@elis.org"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getHeader("Authorization");

    }
}
