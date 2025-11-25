package ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;

import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoRequisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoRequisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Trecho;
import ufg.br.fabricasw.jatai.swsisviagem.domain.UnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.VeiculoProprio;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoTransporte;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.ParamFieldName;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.SupportsCustomizedBinding;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.service.VeiculoProprioService;

/**
 * Created by Carlos on
 */
@Entity
@SupportsCustomizedBinding
@DynamicUpdate
public class Requisicao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ParamFieldName("requisicao_id")
    private Long id;

    private LocalDate dataRequisicao;

    private String titulo;

    private String justificativa;

    private Boolean modificada = false;

    private String justificativaPrestacao;

    private Boolean modoEdicaoHabilitado = false;

    private String anexo;

    private String proponente;

    private LocalDate dataFeitaRequisicao = LocalDate.now();

    private boolean temJustificativaPedidoForaPrazoLegal = false;

    private String descricaoAtividadesDesenvolvidas = "";

    private LocalDateTime dataSubmissao;

    @OneToOne(cascade = CascadeType.ALL)
    private InfoRequisicaoScdp infoScdp;

    @OneToOne
    private UnidadeDepartamento unidade;

    @OneToOne
    private Pessoa proposto;

    @OneToOne(cascade = CascadeType.ALL)
    private VeiculoProprio documentoVeiculoProprio;

    @Enumerated(EnumType.STRING)
    private EEstadoRequisicao estadoRequisicao = EEstadoRequisicao.PREENCHENDO;

    @Enumerated(EnumType.STRING)
    private ETipoRequisicao tipoRequisicao;

    // Pai -> filhos; mantém dono no filho (mappedBy) e remove órfãos
    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "requisicao",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<RequisicaoPassageiro> requisicaoPassageiros = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "viagem_requisicaos",
            joinColumns = @JoinColumn(name = "requisicaos_id"),
            inverseJoinColumns = @JoinColumn(name = "viagem_id")
    )
    @JsonIgnore
    private List<Viagem> viagens = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy(value = "tipoSolicitacao")
    private List<Solicitacao> solicitacoes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Anexo> anexos = new ArrayList<>();

    @OneToMany
    private List<Anexo> prestacaoDeContas = new ArrayList<>();

    @OneToOne(orphanRemoval = true, optional = true)
    @JsonIgnore
    private RelatorioViagemRequisicaoProposto relatorioViagemProposto;

    // ===== Helpers de consistência do lado dono =====
    public void addRequisicaoPassageiro(RequisicaoPassageiro rp) {
        if (rp == null) return;
        this.requisicaoPassageiros.add(rp);
        rp.setRequisicao(this);
    }

    public void removeRequisicaoPassageiro(RequisicaoPassageiro rp) {
        if (rp == null) return;
        this.requisicaoPassageiros.remove(rp);
        rp.setRequisicao(null);
    }
    // ================================================

    public LocalDate getDataFeitaRequisicao() {
        return dataFeitaRequisicao;
    }
    public void setDataFeitaRequisicao(LocalDate dataFeitaRequisicao) {
        this.dataFeitaRequisicao = dataFeitaRequisicao;
    }

    public Boolean hasDiaria() {
        return this.getSolicitacoes().stream().anyMatch(Solicitacao::isDiaria);
    }

    public Boolean hasPassagem() {
        return this.getSolicitacoes().stream().anyMatch(Solicitacao::isPassagem);
    }

    // getQuantidadeMensagensDiaria()

    public int getQuantidadeMensagensDiaria() {
        AtomicInteger count = new AtomicInteger();
        this.getSolicitacoes().forEach(solicitacao -> {
            if (solicitacao.isDiaria()) {
                count.addAndGet(solicitacao.getMensagens().size());
            }
        });
        return count.get();
    }

    //getQuantidadeMensagensPassagem
    //getQuantidadeMensagensTransporte

    public int getQuantidadeMensagensTransporte() {
        AtomicInteger count = new AtomicInteger();
        this.getSolicitacoes().forEach(solicitacao -> {
            if (solicitacao.isTransporte()) {
                count.addAndGet(solicitacao.getMensagens().size());
            }
        });
        return count.get();
    }

    public int getQuantidadeMensagensPassagem() {
        AtomicInteger count = new AtomicInteger();
        this.getSolicitacoes().forEach(solicitacao -> {
            if (solicitacao.isPassagem()) {
                count.addAndGet(solicitacao.getMensagens().size());
            }
        });
        return count.get();
    }

    public String getJustificativaPrestacao() {
        return justificativaPrestacao;
    }

    public void setJustificativaPrestacao(String justificativaPrestacao) {
        this.justificativaPrestacao = justificativaPrestacao;
    }

    public Boolean getModificada() {
        return modificada;
    }

    public boolean getTemJustificativaPedidoForaPrazoLegal() {
        return temJustificativaPedidoForaPrazoLegal;
    }

    public boolean isTemJustificativaPedidoForaPrazoLegal() {
        return temJustificativaPedidoForaPrazoLegal;
    }

    public void setTemJustificativaPedidoForaPrazoLegal(boolean temJustificativaPedidoForaLrazoLegal) {
        this.temJustificativaPedidoForaPrazoLegal = temJustificativaPedidoForaLrazoLegal;
    }

    public void setModificada(Boolean modificada) {
        this.modificada = modificada;
    }

    public List<Passageiro> getPassageiros() {
        List<Passageiro> passageiros = new ArrayList<>();
        this.getRequisicaoPassageiros().forEach((reqPass) -> passageiros.add(reqPass.getPassageiro()));
        return passageiros;
    }

    public List<Anexo> getPrestacaoDeContas() {
        return prestacaoDeContas;
    }

    public void setPrestacaoDeContas(List<Anexo> prestacaoDeContas) {
        this.prestacaoDeContas = prestacaoDeContas;
    }

    public List<Anexo> getAnexos() {
        return anexos;
    }

    public String getAnexo() {
        return anexo;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    public void setAnexos(List<Anexo> anexos) {
        this.anexos = anexos;
    }

    public UnidadeDepartamento getUnidade() {
        return unidade;
    }

    public void setUnidade(UnidadeDepartamento unidade) {
        this.unidade = unidade;
    }

    public ETipoRequisicao getTipoRequisicao() {
        return tipoRequisicao;
    }

    public void setTipoRequisicao(ETipoRequisicao tipoRequisicao) {
        this.tipoRequisicao = tipoRequisicao;
    }

    public Pessoa getProposto() {
        return proposto;
    }

    public void setProposto(Pessoa proposto) {
        this.proposto = proposto;
    }

    public Requisicao() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantidadeSolicitacoes() {
        return this.getSolicitacoes().size();
    }

    public LocalDate getDataRequisicao() {
        return dataRequisicao;
    }

    public void setDataRequisicao(LocalDate dataRequisicao) {
        this.dataRequisicao = dataRequisicao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public List<Solicitacao> getSolicitacoes() {
        return solicitacoes;
    }

    public void setSolicitacoes(List<Solicitacao> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }

    public EEstadoRequisicao getEstadoRequisicao() {
        return estadoRequisicao;
    }

    public void setEstadoRequisicao(EEstadoRequisicao estadoRequisicao) {
        this.estadoRequisicao = estadoRequisicao;
    }

    public void setproposto(Pessoa proposto) {
        this.setProposto(proposto);
        this.setProponente(proposto.getNome());
    }

    public String getProponente() {
        return proponente;
    }

    public void setProponente(String proponente) {
        this.proponente = proponente;
    }

    public VeiculoProprio getDocumentoVeiculoProprio() {
        return documentoVeiculoProprio;
    }

    public void setDocumentoVeiculoProprio(VeiculoProprio documentoVeiculoProprio) {
        this.documentoVeiculoProprio = documentoVeiculoProprio;
    }

    public List<Trecho> allTrechos() {
        List<Trecho> list = new ArrayList<>();
        for (Solicitacao s : getSolicitacoes()) {
            list.addAll(s.getTrechosIda());
            list.addAll(s.getTrechosVolta());
        }
        return list;
    }

    public List<Trecho> getTrechosDeIda() {
        List<Trecho> list = new ArrayList<>();
        getSolicitacoes().forEach(solicitacao -> list.addAll(solicitacao.getTrechosIda()));
        return list;
    }

    public List<Trecho> getTrechosDeVolta() {
        List<Trecho> list = new ArrayList<>();
        getSolicitacoes().forEach((solicitacao) -> list.addAll(solicitacao.getTrechosVolta()));
        return list;
    }

    public void addAnexo(Anexo anexo) { getAnexos().add(anexo); }

    public void removeAnexo(Anexo anexo) { getAnexos().remove(anexo); }

    public void removerSolicitacao(Solicitacao solicitacao) { this.solicitacoes.remove(solicitacao); }

    /**
     * @return the requisicaoPassageiros
     */
    public List<RequisicaoPassageiro> getRequisicaoPassageiros() {
        return requisicaoPassageiros;
    }

    /**
     * @param requisicaoPassageiros the requisicaoPassageiros to set
     */
    public void setRequisicaoPassageiros(List<RequisicaoPassageiro> requisicaoPassageiros) {
        this.requisicaoPassageiros = requisicaoPassageiros;
    }

    public SolicitacaoTransporte getSolicitacaoTransporte() {
        for (Solicitacao s : this.getSolicitacoes()) {
            if (s.isTransporte()) {
                return (SolicitacaoTransporte) s;
            }
        }
        return null;
    }

    public Boolean isSubmetida() {
        return this.getEstadoRequisicao().equals(EEstadoRequisicao.SUBMETIDA);
    }

    public Boolean isTransporteSubmetido() {
        return this.getSolicitacoes().stream().anyMatch((solicitacao) ->
                solicitacao.isTransporte() && solicitacao.isSubmetida()
        );
    }

    public LocalDateTime getDataSubmissao() {
        return dataSubmissao;
    }

    public void setDataSubmissao(LocalDateTime dataSubmissao) {
        this.dataSubmissao = dataSubmissao;
    }

    public void setDiaria(Solicitacao solicitacao) {
        if (solicitacao != null && solicitacao.isDiaria()) {
            this.getSolicitacoes().add(solicitacao);
        }
    }

    public void setTransporte(Solicitacao solicitacao) {
        if (solicitacao != null && solicitacao.isTransporte()) {
            this.getSolicitacoes().add(solicitacao);
        }
    }

    public void setPassagem(Solicitacao solicitacao) {
        if (solicitacao != null && solicitacao.isPassagem()) {
            this.getSolicitacoes().add(solicitacao);
        }
    }

    public Solicitacao getDiaria() {
        for (Solicitacao solicitacao : this.getSolicitacoes()) {
            if (solicitacao.isDiaria()) {
                return solicitacao;
            }
        }
        return null;
    }

    public Solicitacao getPassagem() {
        for (Solicitacao solicitacao : this.getSolicitacoes()) {
            if (solicitacao.isPassagem()) {
                return solicitacao;
            }
        }
        return null;
    }

    public Solicitacao getTransporte() {
        for (Solicitacao solicitacao : this.getSolicitacoes()) {
            if (solicitacao.isTransporte()) {
                return solicitacao;
            }
        }
        return null;
    }

    public Boolean possuiVeiculoProprio() {
        return this.getDocumentoVeiculoProprio() != null;
    }

    public Boolean isSalva() {
        return this.getId() != null;
    }

    public InfoRequisicaoScdp getInfoScdp() {
        return infoScdp;
    }

    public void setInfoScdp(InfoRequisicaoScdp infoScdp) {
        this.infoScdp = infoScdp;
    }

    public boolean foiLancadoNoSCDP() {
        return this.infoScdp != null;
    }

    public void inserirDatasDeEnvioSolicitacoes() {
        ZoneId fusoHorarioDeSaoPaulo = ZoneId.of("America/Sao_Paulo");
        if (!this.hasTransporte()) {
            this.solicitacoes.forEach(solicitacao ->
                    solicitacao.setEnviadoUnidadeEm(LocalDateTime.now(fusoHorarioDeSaoPaulo))
            );
        } else {
            this.getTransporte().setEnviadoUnidadeEm(LocalDateTime.now(fusoHorarioDeSaoPaulo));
        }
    }

    public void atualizarDatasDeEnvioSolicitacoes() {
        ZoneId fusoHorarioDeSaoPaulo = ZoneId.of("America/Sao_Paulo");
        this.solicitacoes.forEach(solicitacao -> {
            if (solicitacao.getEnviadoUnidadeEm() == null && !solicitacao.isTransporte()) {
                solicitacao.setEnviadoUnidadeEm(LocalDateTime.now(fusoHorarioDeSaoPaulo));
            }
        });
    }

    public boolean isEditavel() {
        return !this.estadoRequisicao.equals(EEstadoRequisicao.SUBMETIDA) || this.getModoEdicaoHabilitado();
    }

    public boolean isDevolvida() {
        return this.estadoRequisicao.equals(EEstadoRequisicao.DEVOLVIDA);
    }

    public Boolean getModoEdicaoHabilitado() {
        return modoEdicaoHabilitado;
    }

    public void setModoEdicaoHabilitado(Boolean modoEdicaoHabilitado) {
        this.modoEdicaoHabilitado = modoEdicaoHabilitado;
    }

    public void changeStatusSolicitacoes(EEstadoSolicitacao estado) {
        this.getSolicitacoes().forEach(solicitacao -> solicitacao.setEstadoSolicitacao(estado));
    }

    public Boolean hasTransporte() {
        return this.getSolicitacoes().stream().anyMatch(Solicitacao::isTransporte);
    }

    public Boolean hasTransporteEncerrado() {
        return this.getSolicitacoes().stream().anyMatch((solicitacao) ->
                solicitacao.isTransporte() && solicitacao.isEncerrada()
        );
    }

    public Boolean hasTransporteRecusado() {
        return this.getSolicitacoes().stream().anyMatch((solicitacao) ->
                solicitacao.isTransporte() && solicitacao.isRecusada()
        );
    }

    public boolean verificarNecessidadeRemovacaoDocVeiculoProprio(VeiculoProprioService veiculoProprioService) {
        boolean isRemoverDocVeiculoProprio = true;
        for (Trecho trecho : this.allTrechos()) {
            if (trecho.isDeVeiculoProprio()) {
                isRemoverDocVeiculoProprio = false;
                break;
            }
        }
        if (isRemoverDocVeiculoProprio) {
            if (this.documentoVeiculoProprio != null) {
                veiculoProprioService.apagar(documentoVeiculoProprio);
                this.setDocumentoVeiculoProprio(null);
            } else {
                isRemoverDocVeiculoProprio = false;
            }
        }
        return isRemoverDocVeiculoProprio;
    }

    public RelatorioViagemRequisicaoProposto getRelatorioViagemProposto() {
        return relatorioViagemProposto;
    }

    public void setRelatorioViagemProposto(RelatorioViagemRequisicaoProposto relatorioViagemProposto) {
        this.relatorioViagemProposto = relatorioViagemProposto;
    }

    public List<Viagem> getViagens() {
        return viagens;
    }

    public void setViagens(List<Viagem> viagens) {
        this.viagens = viagens;
    }

    public String getDescricaoAtividadesDesenvolvidas() {
        return descricaoAtividadesDesenvolvidas;
    }

    public void setDescricaoAtividadesDesenvolvidas(String descricaoAtividadesDesenvolvidas) {
        this.descricaoAtividadesDesenvolvidas = descricaoAtividadesDesenvolvidas;
    }

    // ===== equals/hashCode à prova de proxy (baseado só no ID) =====
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Requisicao other = (Requisicao) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public Boolean hasRealtorioViagemProposto() {
        return this.relatorioViagemProposto != null;
    }

    // toString que não força inicializar coleções LAZY
    @Override
    public String toString() {
        return "Requisicao{"
                + "\n\tid=" + getId()
                + ", \n\tdataRequisicao='" + getDataRequisicao() + '\''
                + ", \n\ttitulo='" + getTitulo() + '\''
                + ", \n\tjustificativa='" + getJustificativa() + '\''
                + ", \n\tmodificada=" + getModificada()
                + ", \n\tjustificativaPrestacao='" + getJustificativaPrestacao() + '\''
                + ", \n\tqtdPassageiros=" + (requisicaoPassageiros != null ? requisicaoPassageiros.size() : 0)
                + ", \n\tqtdSolicitacoes=" + (solicitacoes != null ? solicitacoes.size() : 0)
                + ", \n\tqtdPrestacaoDeContas=" + (getPrestacaoDeContas() != null ? getPrestacaoDeContas().size() : 0)
                + ", \n\tanexo='" + getAnexo() + '\''
                + ", \n\testadoRequisicao=" + getEstadoRequisicao()
                + ", \n\tunidade=" + getUnidade()
                + ", \n\ttipoRequisicao=" + getTipoRequisicao()
                + ", \n\tproposto=" + getProposto()
                + ", \n\tdocumentoVeiculoProprio=" + getDocumentoVeiculoProprio()
                + ", \n\tproponente='" + getProponente() + '\''
                + "\n}";
    }
}