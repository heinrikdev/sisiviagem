package ufg.br.fabricasw.jatai.swsisviagem.repository.projection;

/**
 * 
 *
 * @author Ronaldo N. de Sousa
 */
public interface VeiculoSimpleProjection {

    Long getId();
    
    String getPlaca();
    
    String getCarro();
    
    String getCombustivel();
    
    String getMarca();
    
    String getAno();
    
    String getModelo();
}
