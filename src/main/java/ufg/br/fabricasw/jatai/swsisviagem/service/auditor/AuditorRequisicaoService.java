package ufg.br.fabricasw.jatai.swsisviagem.service.auditor;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EUnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.repository.auditor.AuditorRequisicaoRepository;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 * Service que faz comunicação com o repositorio capaz de realizar as operações
 * do auditor.
 * @author ronogue
 */
@Service("auditor_requisicao_service")
public class AuditorRequisicaoService {
    
    @Autowired
    private AuditorRequisicaoRepository repository;
    
    @Autowired
    private UsuarioService usuarioService;

    public Page<Requisicao> findAll(Pageable pageable) {
        return this.repository.findAll(this.getDepartamento(), pageable);
    }
    
    public Page<Requisicao> findAll(EEstadoSolicitacao status, Integer ano, Pageable pageable) {
        
        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.repository.findAllByStatus(this.getDepartamento(), status, ano,pageable);
        }
        
        return this.repository.findAllByStatus(this.getDepartamento(), ano, pageable);
    }
    
    public Page<Requisicao> findAll(EEstadoSolicitacao status, LocalDate data, Pageable pageable) {
        
        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.repository.findAllByStatus(this.getDepartamento(), status, data, pageable);
        }
        
        return this.repository.findAllByStatus(this.getDepartamento(), data, pageable);
    }
    
    public Page<Requisicao> findAll(EEstadoSolicitacao status, LocalDate from, LocalDate to, Pageable pageable) {
        
        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.repository.findAllByStatus(this.getDepartamento(), status, from, to, pageable);
        }
        
        return this.repository.findAllByStatus(this.getDepartamento(), from, to, pageable);
    }
    
    public Page<Requisicao> findAllByRequisicao(Long requisicao_id, Pageable pageable) {
        return this.repository.findAllByRequisicao(this.getDepartamento(), requisicao_id, pageable);
    }
    
    public Page<Requisicao> findAllByProposto(String proposto_nome, Pageable pageable) {
        return this.repository.findAllByProposto(this.getDepartamento(), proposto_nome, pageable);
    }
    
    private EUnidadeDepartamento getDepartamento() {
        return this.usuarioService.findUserLogado().getUnidadeDepartamento();
    }
}
