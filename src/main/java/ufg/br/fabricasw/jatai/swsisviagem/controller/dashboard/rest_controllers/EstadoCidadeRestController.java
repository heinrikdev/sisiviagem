package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ufg.br.fabricasw.jatai.swsisviagem.domain.estados_municipios.Estado;
import ufg.br.fabricasw.jatai.swsisviagem.domain.estados_municipios.Municipio;
import ufg.br.fabricasw.jatai.swsisviagem.service.estados_municipios.EstadoMunicipioService;

import java.util.List;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@RestController
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/dashboard/api/cidadeEstado")
@CrossOrigin("*")
public class EstadoCidadeRestController {

    @Autowired
    private EstadoMunicipioService estadoMunicipioService;
    
    @GetMapping("/{estado}")
    public List<Municipio> findAllMunicipioBy(@PathVariable("estado")String estado) {
        return this.estadoMunicipioService.findAllMunicipiosByUf(estado);
    }

    @GetMapping("/estados")
    public List<Estado> findAllEstados() {
        return this.estadoMunicipioService.findAllEstados();
    }
}
