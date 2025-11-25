package ufg.br.fabricasw.jatai.swsisviagem.repository.projection;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDate;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.json.LocalDateSerializer;

/**
 *
 * @author Ronaldo N. de Sousa
 */
public interface ViagemInfoProjection {
    
    Long getId();
    
    String getTitulo();
    
    @JsonSerialize(using = LocalDateSerializer.class)
    LocalDate getData();
    
    String getDescricao();
    
    String getStartHora();
}
