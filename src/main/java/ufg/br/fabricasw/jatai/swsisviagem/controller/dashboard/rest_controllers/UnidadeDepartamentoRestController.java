package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.rest_controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufg.br.fabricasw.jatai.swsisviagem.domain.UnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.service.UnidadeService;

/**
 * Controller para fornecer as unidades cadastradas por meio de requisições Ajax
 * @author ronogue
 */
@RestController
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/dashboard/api/unidades")
@CrossOrigin("*")
public class UnidadeDepartamentoRestController {
    
    @Autowired
    private UnidadeService unidadeService;
    
    /**
     * Retorna as unidades conforme to tipo de requisição.
     * @param tipoRequisicao - 'unidadesAdministrativa', 'unidadesGraduacao', 'unidadesPos'
     * @return - Lista com as unidades em JSON.
     */
    @GetMapping("/{tipoRequisicao}")
    public List<UnidadeDepartamento> findByTipoRequisicao(@PathVariable("tipoRequisicao") String tipoRequisicao) {
      
        return this.unidadeService.findByTipoRequisicao(tipoRequisicao);
    }
    
}
