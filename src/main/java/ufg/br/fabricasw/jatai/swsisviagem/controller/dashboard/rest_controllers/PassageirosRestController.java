package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.rest_controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.service.PassageiroService;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoPassageiroService;

/**
 * 
 * @author Ronaldo N. de Sousa
 */
@RestController
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/dashboard/api/passageiro")
@CrossOrigin("*")
public class PassageirosRestController {

    @Autowired
    private RequisicaoPassageiroService requisicaoPassageiroService;

    @Autowired
    private PassageiroService passageiroService;

    @GetMapping("/status/ida/{estado}/{passageiro_id}")
    public void estadoIda(@PathVariable("estado") EEstadoPassageiro estado,
            @PathVariable("passageiro_id") Long id_requisicao_passageiro) {
        this.requisicaoPassageiroService.atualizarEstadoIda(estado, id_requisicao_passageiro);
    }

    @GetMapping("/status/volta/{estado}/{passageiro_id}")
    public void estadoVolta(@PathVariable("estado") EEstadoPassageiro estado,
            @PathVariable("passageiro_id") Long id_requisicao_passageiro) {
        this.requisicaoPassageiroService.atualizarEstadoVolta(estado, id_requisicao_passageiro);
    }

    @GetMapping("/nome/{nome}")
    public List<Passageiro> estadoVolta(@PathVariable("nome") String nome) {
        return passageiroService.findAllBy(nome);
    }

    @GetMapping("/{passageiro_id}")
    public ResponseEntity<Passageiro> findPassageiro(@PathVariable("passageiro_id") Long passageiro_id) {

        Passageiro passageiro = this.passageiroService.findOne(passageiro_id);
        return ResponseEntity.ok(passageiro);
    }
}
