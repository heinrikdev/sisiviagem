package ufg.br.fabricasw.jatai.swsisviagem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RequisicaoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;
import ufg.br.fabricasw.jatai.swsisviagem.repository.RequisicaoPassageiroRepository;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Service
public class RequisicaoPassageiroService {

    @Autowired
    private RequisicaoPassageiroRepository repository;
    
    @Autowired
    private PassageiroService passageiroService;
    
    public void atualizarEstadoIda(EEstadoPassageiro estado, Long id) {
        this.repository.atualizarEstadoIda(estado, id);
    }
    
    public void atualizarEstadoVolta(EEstadoPassageiro estado, Long id) {
        this.repository.atualizarEstadoVolta(estado, id);
    }
    
    public void removerPassageiroDaRequisicao(Long requisicao_passageiro_id) {
        this.repository.removerPassageiroDaRequisicao(requisicao_passageiro_id);
    }
    
    public RequisicaoPassageiro find(Long id) {
        return this.repository.getOne(id);
    }
    
    public RequisicaoPassageiro salvar(RequisicaoPassageiro requisicaoPassageiro) {
        
        if (requisicaoPassageiro.getPassageiro().getId() == null) {
        
            Passageiro passageiro = this.passageiroService.save(requisicaoPassageiro.getPassageiro());
            requisicaoPassageiro.setPassageiro(passageiro);
        }
        
        return this.repository.save(requisicaoPassageiro);
    }
}
