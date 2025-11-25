package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.motorista.viagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoTarefa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Tarefa;
import ufg.br.fabricasw.jatai.swsisviagem.service.TarefaService;

/**
 *
 * @author Ronaldo N Sousa
 * Criado em: Dec 28, 2018
 */
@Controller("motorista_viagem_avaliar_tarefa")
@RequestMapping("/dashboard/motorista/viagem")
@PreAuthorize("hasAuthority('MOTORISTA')")
public class AvaliarTarefaController {
    
    private static final String REDIRECT_TO_VIEW_DETALHES_VIAGEM = "redirect:/dashboard/motorista/viagem/detalhes/";

    @Autowired
    private TarefaService tarefaService;
    
    @PostMapping("avaliar_tarefa")
    public String avaliar
    (
        @RequestParam(name = "tarefa_id", required = true) Long tarefa_id,
        @RequestParam(name = "observacao_tarefa", required = false) String observacao,
        @RequestParam(name = "estado", required = true) Boolean isConcluida
    ) {
        
        Tarefa tarefa = this.tarefaService.findOne(tarefa_id);
        tarefa.setObservacao(observacao);
        
        if (isConcluida) {
            
            tarefa.setEstadoTarefa(EEstadoTarefa.Concluida);
        }
        else {
            
            tarefa.setEstadoTarefa(EEstadoTarefa.NaoRealizada);
        }
        
        this.tarefaService.update(tarefa);
        return REDIRECT_TO_VIEW_DETALHES_VIAGEM + this.tarefaService.findViagemIdFromTarefa(tarefa_id);
    }
}
