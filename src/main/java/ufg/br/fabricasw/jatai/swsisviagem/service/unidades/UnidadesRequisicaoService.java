
package ufg.br.fabricasw.jatai.swsisviagem.service.unidades;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.repository.unidades.UnidadesRequisicaoRepository;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 * Service que prover ações no banco de dados em relação a requisição gerenciadas
 * pelas unidades.
 * 
 * @author ronogue
 */
@Service("unidades_requisicao_service")
public class UnidadesRequisicaoService {
    
    @Autowired
    private UnidadesRequisicaoRepository repository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Busca todas a requisições submetidas para o perfil da unidade logada no
     * sistema. 
     * 
     * @param pageable informações da página.
     * @return página contendo uma lista de requisições.
     */
    public Page<Requisicao> findAll(Pageable pageable) {
        
        String status = EEstadoSolicitacao.SUBMETIDA.toString().toUpperCase();
        
        return this.repository.findAllByStatus(this.getDepartamentoId(), status, pageable);
    }
    
    public Page<Requisicao> findAllByRequisicao(Long requisicao_id, Pageable pageable) {
        return this.repository.findAllByRequisicaoId(this.getDepartamentoId(), requisicao_id, pageable);
    }
    
    public Page<Requisicao> findAllByStatus(String status, Pageable pageable) {
        return this.repository.findAllByStatus(this.getDepartamentoId(), status, pageable);
    }
    
    public Page<Requisicao> findAllByStatus(EEstadoSolicitacao status, Integer ano, Pageable pageable) {
        
        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.repository.findAllByStatus(this.getDepartamentoId(), status.toString(), ano, pageable);
        }
        
        return this.repository.findAllByStatus(this.getDepartamentoId(), ano, pageable);
    }
    
    public Page<Requisicao> findAllByStatus(EEstadoSolicitacao status, LocalDate data, Pageable pageable) {
        
        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.repository.findAllByStatus(this.getDepartamentoId(), status.toString(), data, pageable);
        }
        
        return this.repository.findAllByStatus(this.getDepartamentoId(), data, pageable);
    }
    
    public Page<Requisicao> findAllByStatus(EEstadoSolicitacao status, LocalDate from, LocalDate to, Pageable pageable) {
        
        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.repository.findAllByStatus(this.getDepartamentoId(), status.toString(), from, to, pageable);
        }
        
        return this.repository.findAllByStatus(this.getDepartamentoId(), from, to, pageable);
    }
    
    public Page<Requisicao> findAllByProposto(String proposto_nome, Pageable pageable) {
        return this.repository.findAllByProposto(this.getDepartamentoId(), proposto_nome, pageable);
    }

    private Long getDepartamentoId() {
        return this.usuarioService.findUserLogado().getUnidade().getId();
    }
}
