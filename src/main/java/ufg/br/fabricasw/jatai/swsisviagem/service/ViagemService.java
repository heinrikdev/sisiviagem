package ufg.br.fabricasw.jatai.swsisviagem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoViagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.repository.ViagemRepository;
import ufg.br.fabricasw.jatai.swsisviagem.repository.projection.ViagemInfoProjection;

import java.time.LocalDate;
import java.util.List;

@Service
public class ViagemService implements AbstractService<Viagem, Long> {

    @Autowired
    private ViagemRepository repository;
    
    @Autowired
    private UsuarioService usuarioService;

    @Override
    public Viagem save(Viagem entidade) {
        return repository.save(entidade);
    }

    @Override
    public void apagar(Viagem entidade) {
        repository.delete(entidade);
    }

    @Override
    public Viagem atualizar(Viagem entidade) {
        return repository.save(entidade);
    }

    @Override
    public Viagem find(Viagem entidade) {
        return null;
    }

    @Override
    public Viagem find(Long id) {
        return repository.getOne(id);
    }

    @Override
    public List<Viagem> findAll() {
        return (List<Viagem>) repository.findAll();
    }

    public List<Viagem> findByData(LocalDate data) {
        return repository.findByData(data);
    }

    public List<Viagem> findByRangeDate(LocalDate from, LocalDate to, EEstadoViagem estadoViagem) {
        return repository.findByRangeDate(from, to, estadoViagem);
    }
    
    public List<Viagem> findByRangeDate(Long reqId, LocalDate from, LocalDate to, EEstadoViagem estadoViagem) {
        return repository.findParaRequisicao(reqId,from, to, estadoViagem);
    }

    public List<Viagem> findByMotorista(String motorista) {
        return repository.findByMotorista(motorista);
    }
    
    public Page<Viagem> findByMotoristaLogado(Pageable pageable) {
        
        final Long motorista_id = this.getMotoristaId();
        return repository.findByMotorista(motorista_id, pageable);
    }
    
    
    public List<Viagem> findByMotoristaandDate(String motorista, String date) {
        return repository.findByMotoristaAndDate(motorista, date);
    }
    
    /**
     * Busca todas as viagens.
     * @param pageable descrição da página.
     * @return Page
     */
    public Page<Viagem> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }
    
    /**
     * Busca todas as viagem por estado da mesma.
     * @param status estado da viagem: EEstadoViagem  
     * @param pageable informações da página
     * @return 
     */
    public Page<Viagem> findAllByStatus(EEstadoViagem status, Pageable pageable) {
        return this.repository.findAllByStatus(status, pageable);
    }
    
    /**
     * Adiciona uma requisição a uma viagem.
     * @param viagem_id identificador da viagem
     * @param requisicao_id  identificador da requisição
     */
    public void adicionarRequisicao(Long viagem_id, Long requisicao_id) {
        this.repository.adicionarRequisicao(viagem_id, requisicao_id);
    }
    
    /**
     * Busca todas as viagens progamadas para atender uma requisicao.
     * @param requisicao_id id da requisicao.
     * @return 
     */
    public List<ViagemInfoProjection> findAllViagemProgramadaPara(Long requisicao_id) {
        return this.repository.findAllViagemProgramadaPara(requisicao_id);
    }

    public Page<Viagem> findById(Long value, Pageable pageable) {
        return this.repository.findById(value, pageable);
    }
    
    private Long getMotoristaId() {
        return this.usuarioService.findUserLogado().getPessoa().getId();
    }

    public List<String> fetchMotoristaNomes(Long viagem_id) {
        return this.repository.fetchMotoristaNomes(viagem_id);
    }

    public void deleteVeiculoViagem(Long viagemId, Long veiculoId){
        this.repository.deleteVeiculoViagem(viagemId, veiculoId);
    }
}
