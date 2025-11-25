package ufg.br.fabricasw.jatai.swsisviagem.service.estados_municipios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.estados_municipios.Estado;
import ufg.br.fabricasw.jatai.swsisviagem.domain.estados_municipios.Municipio;
import ufg.br.fabricasw.jatai.swsisviagem.repository.estados_municipios.IEstadoRepository;
import ufg.br.fabricasw.jatai.swsisviagem.repository.estados_municipios.IMunicipioRepository;

/**
 *
 * @author ronogue
 */
@Service
public class EstadoMunicipioService {
    
    @Autowired
    private IEstadoRepository estadoRepository;
    
    @Autowired
    private IMunicipioRepository municipioRepository;
    
    public List<Estado> findAllEstados() {
        return this.estadoRepository.findAll();
    }
    
    public List<Municipio> findAllMunicipiosByUf(String uf) {
        return this.municipioRepository.findAllByUf(uf);
    } 
}
