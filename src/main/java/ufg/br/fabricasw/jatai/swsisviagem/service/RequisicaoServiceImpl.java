package ufg.br.fabricasw.jatai.swsisviagem.service;

import java.time.LocalDate;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.UnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoRequisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EUnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RequisicaoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.repository.RequisicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.repository.transporte.RequisicaoTransporteRepository;

/**
 * Created by thevilela on 01/02/17.
 */
@Service
public class RequisicaoServiceImpl implements AbstractService<Requisicao, Long> {

    @Autowired
    private RequisicaoRepository requisicaoRepository;

    @Autowired
    private RequisicaoTransporteRepository requisicaoTransporteRepository;

    public Requisicao findById(Long id) {
        return requisicaoRepository.getOne(id);
    }

    public void updateDescricaoAtividadesDesenvolvidas(String descricaoAtividadesDesenvolvidas, Long requisicao_id) {
        this.requisicaoRepository.updateDescricaoAtividadesDesenvolvidas(descricaoAtividadesDesenvolvidas,
                requisicao_id);
    }

    public void addPrestacaoContas(Long requisicao_id, Long anexo_id) {
        this.requisicaoRepository.addPrestacaoContas(requisicao_id, anexo_id);
    }

    @Override
    @Transactional(rollbackFor = { DataAccessException.class, InvalidDataAccessApiUsageException.class,
            Exception.class })
    public Requisicao save(Requisicao entidade) {
        return requisicaoRepository.saveAndFlush(entidade);
    }

    @Override
    public void apagar(Requisicao entidade) {
        requisicaoRepository.delete(entidade);
    }

    @Override
    @Transactional(rollbackFor = { DataAccessException.class, InvalidDataAccessApiUsageException.class,
            Exception.class })
    public Requisicao atualizar(Requisicao entidade) {
        return requisicaoRepository.save(entidade);
    }

    @Override
    public Requisicao find(Requisicao entidade) {
        return requisicaoRepository.getOne(entidade.getId());
    }

    @Override
    public Requisicao find(Long id) {
        return requisicaoRepository.getOne(id);
    }

    @Override
    public List<Requisicao> findAll() {
        return requisicaoRepository.findAll();
    }

    public List<Requisicao> findSubmetidas() {
        return requisicaoRepository.findByEstado(EEstadoRequisicao.SUBMETIDA);
    }

    @Transactional(readOnly = true)
    public Requisicao findForEdicao(Long id) {
        Requisicao r = requisicaoRepository.fetchForEdicaoBase(id)
                .orElseThrow(() -> new javax.persistence.EntityNotFoundException("Requisicao " + id + " não encontrada"));

        // aquece a outra "bag"
        requisicaoRepository.findSolicitacoesOf(id);
        Hibernate.initialize(r.getSolicitacoes());

        // doc veículo
        Hibernate.initialize(r.getDocumentoVeiculoProprio());

        // inicializa passageiros sem trocar a lista
        Hibernate.initialize(r.getRequisicaoPassageiros());
        List<RequisicaoPassageiro> lista = r.getRequisicaoPassageiros();

        // Se quiser "desproxificar", substitua **elementos** SEM trocar a instância da lista
        for (int i = 0; i < lista.size(); i++) {
            RequisicaoPassageiro rp = lista.get(i);
            RequisicaoPassageiro unproxied = unproxy(rp);
            Hibernate.initialize(unproxied.getPassageiro()); // se a view/helper usa
            if (unproxied != rp) {
                lista.set(i, unproxied); // mantém a MESMA lista (PersistentBag), só troca o elemento
            }
        }

        return r;
    }
    @SuppressWarnings("unchecked")
    private static <T> T unproxy(T entity) {
        if (entity instanceof HibernateProxy) {
            return (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
        }
        return entity;
    }
    public List<Requisicao> findRequisicoesUsuario(String username) {
        return requisicaoRepository.listarRequisicoesDoUsuario(username);
    }

    public List<Requisicao> findRequisicoesUsuarioAndDate(String username, String data) {
        return requisicaoRepository.listarByUsuarioAndData(username, data);
    }

    public List<Requisicao> findRequisicaoByUnidade(UnidadeDepartamento unidadeDepartamento) {

        return requisicaoRepository.listarByUnidade(unidadeDepartamento);
    }

    public List<Requisicao> findBySocilitacao(Long idSolicitacao) {
        return requisicaoRepository.findBySolicitacao(idSolicitacao);
    }

    public List<Requisicao> findBySubmetidas() {
        return requisicaoRepository.findBy();
    }

    public List<Requisicao> findAprovadas(EUnidadeDepartamento tipo) {
        return requisicaoRepository.findAllAprovadas(tipo);
    }

    public Page<Requisicao> findAllByProposto(Long id, Pageable pageable) {
        return this.requisicaoRepository.findAllByProposto(id, pageable);
    }

    public void salvar(Requisicao requisicao) {
        this.requisicaoRepository.save(requisicao);
    }

    /**
     * Busca todas as requisições enviadas para o transporte.
     *
     * @param pageable
     * @return
     */
    public Page<Requisicao> findAllRequisicoesTransporte(Pageable pageable) {
        return this.requisicaoTransporteRepository.findAllSubmetidas(pageable);
    }

    /**
     * Muda o status da requisição e solicitações para SUBMETIDA e passa a ser
     * visível para os proponentes conforme as regras.
     *
     * @param id
     */
    public void submeterRequisicao(Long id) {

        Requisicao requisicao = this.find(id);

        requisicao.setEstadoRequisicao(EEstadoRequisicao.SUBMETIDA);

        requisicao.getSolicitacoes().stream()
                .forEach(solicitacao -> solicitacao.setEstadoSolicitacao(EEstadoSolicitacao.SUBMETIDA));

        this.requisicaoRepository.save(requisicao);
    }

    /**
     * Deleta uma requisição.
     *
     * @param id
     */
    public void deletarRequisicao(Long id) {

        Requisicao requisicao = this.find(id);
        this.requisicaoRepository.delete(requisicao);
    }

    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não.
     *
     * @param id       id da requisição
     * @param pageable detem as informações da paginação
     * @return
     */
    public Page<Requisicao> findAllRequisicoesTransporte(Long id, Pageable pageable) {
        return this.requisicaoTransporteRepository.findAllRequisicoes(id, pageable);
    }

    /**
     * Busca a requisição eviada para o transporte conforme o nome do proposto
     * idependente de ter sida aprovada ou não.
     *
     * @param proposto nome do proposto
     * @param pageable detem as informações da paginação
     * @return
     */
    public Page<Requisicao> findAllRequisicoesTransporte(String proposto, Pageable pageable) {
        return this.requisicaoTransporteRepository.findAllRequisicoes(proposto, pageable);
    }

    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não.
     *
     * @param status   estado da solicitação - SUBMETIDA, APROVADA, ENCERRADA,
     *                 RECUSADA
     * @param pageable detem as informações da paginação
     * @return
     */
    public Page<Requisicao> findAllRequisicoesByEstado(EEstadoSolicitacao status, Pageable pageable) {
        return this.requisicaoTransporteRepository.findAllRequisicoesByEstado(status, pageable);
    }

    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não.
     *
     * @param status   estado da solicitação - SUBMETIDA, APROVADA, ENCERRADA,
     *                 RECUSADA
     * @param ano      - ano
     * @param pageable detem as informações da paginação
     * @return
     */
    public Page<Requisicao> findAllRequisicoesByEstado(EEstadoSolicitacao status, Integer ano, Pageable pageable) {

        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.requisicaoTransporteRepository.findAllRequisicoesByEstado(status, ano, pageable);
        }

        return this.requisicaoTransporteRepository.findAllRequisicoesByEstado(ano, pageable);
    }

    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não.
     *
     * @param status   estado da solicitação - SUBMETIDA, APROVADA, ENCERRADA,
     *                 RECUSADA
     * @param data
     * @param pageable detem as informações da paginação
     * @return
     */
    public Page<Requisicao> findAllRequisicoesByEstado(EEstadoSolicitacao status, LocalDate data, Pageable pageable) {

        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.requisicaoTransporteRepository.findAllRequisicoesByEstado(status, data, pageable);
        }

        return this.requisicaoTransporteRepository.findAllRequisicoesByEstado(data, pageable);
    }

    /**
     * Busca a requisição eviada para o transporte conforme o id idependente de
     * ter sida aprovada ou não.
     *
     * @param status   estado da solicitação - SUBMETIDA, APROVADA, ENCERRADA,
     *                 RECUSADA
     * @param from
     * @param to
     * @param pageable detem as informações da paginação
     * @return
     */
    public Page<Requisicao> findAllRequisicoesByEstado(EEstadoSolicitacao status, LocalDate from, LocalDate to,
            Pageable pageable) {

        if (!status.equals(EEstadoSolicitacao.FILTRO_BUSCAR_TODOS)) {
            return this.requisicaoTransporteRepository.findAllRequisicoesByEstado(status, from, to, pageable);
        }

        return this.requisicaoTransporteRepository.findAllRequisicoesByEstado(from, to, pageable);
    }

    /**
     * Busca o nome do proposto de uma determinada requisição.
     *
     * @param requisicao_id identificação da requisição.
     * @return Nome do proposto.
     */
    public String findPropostoNomeRequisicao(Long requisicao_id) {
        return this.requisicaoRepository.findPropostoNomeRequisicao(requisicao_id);
    }

    public Pessoa findPropostoRequisicao(Long requisicao_id) {
        return this.requisicaoRepository.findPropostoRequisicao(requisicao_id);
    }

    public void habilitarOuDesabilitarModoEdicao(Long requisicao_id) {
        this.requisicaoRepository.habilitarOuDesabilitarModoEdicao(requisicao_id);
    }

    public void removerViagem(Long requisicao_id, Long viagem_id) {
        this.requisicaoRepository.removerViagem(requisicao_id, viagem_id);
    }
}
