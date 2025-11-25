package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.viagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Tarefa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;

/**
 * Durante a viagem o motorista poderá ser incubido de realizar algumas tarefas,
 * aqui é o controller responsável por cadastrálas.
 * 
 * @author Ronaldo N. de Sousa
 */
@Controller("transporte_viagem_tarefa")
@RequestMapping("/dashboard/transporte/viagem")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class TarefaController {

    private static final String REDIRECT_LINK_VIAGEM_DETALHES = "redirect:/dashboard/transporte/viagem/${viagem_id}/detalhes";

    @Autowired
    private ViagemService viagemService;

    @PostMapping("/adicionar_tarefa/{viagem_id}")
    public String salvarTarefa(@PathVariable("viagem_id") Long viagem_id, Tarefa tarefa) {

        Viagem viagem = this.viagemService.find(viagem_id);

        if (tarefa.getId() != null) {

            viagem.getTarefas().remove(tarefa);
        }

        viagem.getTarefas().add(tarefa);
        this.viagemService.atualizar(viagem);

        StringBuilder builder = new StringBuilder(REDIRECT_LINK_VIAGEM_DETALHES);
        builder.replace(38, 50, viagem.getId().toString());

        return builder.toString();
    }
}
