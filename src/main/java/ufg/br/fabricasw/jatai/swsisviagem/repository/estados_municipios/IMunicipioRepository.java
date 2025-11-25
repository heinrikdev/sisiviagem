package ufg.br.fabricasw.jatai.swsisviagem.repository.estados_municipios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.estados_municipios.Municipio;

/**
 *
 * @author ronogue
 */
@Repository
public interface IMunicipioRepository extends JpaRepository<Municipio, Long>{
    
    List<Municipio> findAllByUf(String uf);
}
