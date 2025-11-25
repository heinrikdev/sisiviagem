package ufg.br.fabricasw.jatai.swsisviagem.service.proposto;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.repository.proposto.PropostoRequisicaoRepository;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Service("proposto_requisicao_service")
public class PropostoRequisicaoService {

    @Autowired
    private PropostoRequisicaoRepository repository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    public Page<Requisicao> findAll(Pageable pageable) {
        return this.repository.findAll(this.findPropostoLogadoId(), pageable);
    }
    
    public Page<Requisicao> findAllByStatus(EEstadoSolicitacao status,Pageable pageable) {
        return this.repository.findAllByStatus(this.findPropostoLogadoId(), status, pageable);
    }
    
    public Page<Requisicao> findAllByStatus(EEstadoSolicitacao status, Integer ano,Pageable pageable) {
        
        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.repository.findAllByStatus(this.findPropostoLogadoId(), status, ano, pageable);
        }
        
        return this.repository.findAllByStatus(this.findPropostoLogadoId(), ano, pageable);
    }
    
    public Page<Requisicao> findAllByStatus(EEstadoSolicitacao status, LocalDate data,Pageable pageable) {
        
        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.repository.findAllByStatus(this.findPropostoLogadoId(), status, data, pageable);
        }
        
        return this.repository.findAllByStatus(this.findPropostoLogadoId(), data, pageable);
    }
    
    public Page<Requisicao> findAllByStatus(EEstadoSolicitacao status, LocalDate from, LocalDate to,Pageable pageable) {
        
        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.repository.findAllByStatus(this.findPropostoLogadoId(), status, from, to, pageable);
        }
        
        return this.repository.findAllByStatus(this.findPropostoLogadoId(), from, to, pageable);
    }
    
    public Page<Requisicao> findAllByRequisicao(Long requisicao_id, Pageable pageable) {
        return this.repository.findAllByRequisicao(this.findPropostoLogadoId(), requisicao_id, pageable);
    }
    
    private Long findPropostoLogadoId() {
        return this.usuarioService.findUserLogado().getPessoa().getId();
    }
}
