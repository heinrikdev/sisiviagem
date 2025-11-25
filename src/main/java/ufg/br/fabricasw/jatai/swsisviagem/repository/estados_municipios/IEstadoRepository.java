package ufg.br.fabricasw.jatai.swsisviagem.repository.estados_municipios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.estados_municipios.Estado;

/**
 *
 * @author ronogue
 */
@Repository
public interface IEstadoRepository extends JpaRepository<Estado, Long> {
    
}
