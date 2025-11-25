package ufg.br.fabricasw.jatai.swsisviagem.repository.projection;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDate;
import java.util.List;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.json.LocalDateSerializer;

/**
 * Projeção para buscar no banco de dados somente as colunas especificadas aqui.
 * 
 *  Viagem: (94) - ROTA JATAÍ - GOIÂNIA 
    Veículo: ONA – 3715 / COBALT - 1.8 LT
    Motorista: DANIEL DE PAULA CONTI
 * 
 * 
 * @author Ronaldo N. de Sousa
 */
public interface ViagemSimpleProjection {

    Long getId();
    
    String getTitle();
    
    String getTitulo();
    
    @JsonSerialize(using = LocalDateSerializer.class)
    LocalDate getData();
    
    @JsonSerialize(using = LocalDateSerializer.class)
    LocalDate getDataTermino();
    
    String getDescricao();
    
    String getStart();
    
    String getEnd();
    
    String getColor();
    
    List<VeiculoSimpleProjection> getVeiculos();
    
    List<MotoristaNomeProjection> getMotoristas();
}
