package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Tarefa;
import ufg.br.fabricasw.jatai.swsisviagem.service.TarefaService;

/**
 * O motorista poderá ser incumbido de realizar algumas tarefa, este controller
 * lisdar com as ações relacionadas a tarefa.
 * 
 * @author Ronaldo N. de Sousa
 */
@RestController
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/dashboard/api/tarefa")
@CrossOrigin("*")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    @GetMapping("/concluida/{tarefa_id}")
    public void concluirTarefa(@PathVariable("tarefa_id") Long tarefa_id) {
        this.tarefaService.concluirTarefa(tarefa_id);
    }

    @GetMapping("/nao_realizada/{tarefa_id}")
    public void naoRealizadaTarefa(@PathVariable("tarefa_id") Long tarefa_id) {
        this.tarefaService.tarefaNaoRealizada(tarefa_id);
    }

    @GetMapping("/{tarefa_id}")
    public Tarefa findTarefa(@PathVariable("tarefa_id") Long tarefa_id) {
        return this.tarefaService.findOne(tarefa_id);
    }
}
