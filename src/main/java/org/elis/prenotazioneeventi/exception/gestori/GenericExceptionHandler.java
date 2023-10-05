package org.elis.prenotazioneeventi.exception.gestori;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.elis.prenotazioneeventi.dto.response.MessaggioErroreDTO;
import org.elis.prenotazioneeventi.exception.DatiNonValidiException;
import org.elis.prenotazioneeventi.exception.UtenteNonTrovatoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(DatiNonValidiException.class)
    public ResponseEntity<MessaggioErroreDTO> gestisciDatiNonValidiException(DatiNonValidiException e){
        MessaggioErroreDTO m=new MessaggioErroreDTO();
        m.setData(LocalDateTime.now());
        m.setErrori(e.getErrori());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
    }

    @ExceptionHandler(UtenteNonTrovatoException.class)
    public ResponseEntity<MessaggioErroreDTO> utenteNonTrovatoException(UtenteNonTrovatoException e){
        Map<String,String> map=new TreeMap<>();
        map.put("utente",e.getMessage());
        MessaggioErroreDTO m=new MessaggioErroreDTO();
        m.setData(LocalDateTime.now());
        m.setErrori(map);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(m);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<MessaggioErroreDTO> sqlIntegrityConstraintException(SQLIntegrityConstraintViolationException e){
        Map<String,String> map=new TreeMap<>();
        map.put("utente",e.getMessage());
        MessaggioErroreDTO m=new MessaggioErroreDTO();
        m.setData(LocalDateTime.now());
        m.setErrori(map);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessaggioErroreDTO> methodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String,String> map=new TreeMap<>();
        for(FieldError o: e.getFieldErrors()) {
            map.put(o.getField(),o.getDefaultMessage());
        }
        MessaggioErroreDTO m=new MessaggioErroreDTO();
        m.setData(LocalDateTime.now());
        m.setErrori(map);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MessaggioErroreDTO> validationException(ConstraintViolationException e){
        Map<String,String> map=e.getConstraintViolations().stream()
                .collect(Collectors.toMap(c->c.getPropertyPath().toString(), ConstraintViolation::getMessage));
        MessaggioErroreDTO m=new MessaggioErroreDTO();
        m.setData(LocalDateTime.now());
        map.remove("idRichiedente");
        m.setErrori(map);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(m);
    }

}
