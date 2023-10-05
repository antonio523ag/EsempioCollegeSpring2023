package org.elis.prenotazioneeventi.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class MessaggioErroreDTO {

    private LocalDateTime data;
    private Map<String,String> errori;

}
