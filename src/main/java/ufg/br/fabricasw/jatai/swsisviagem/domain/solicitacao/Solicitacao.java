package ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao;

import ufg.br.fabricasw.jatai.swsisviagem.domain.Mensagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Trecho;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoSolicitacao;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by thevilela on 31/01/17.
 */
@Entity
@DiscriminatorColumn(name = "CLASS", discriminatorType = DiscriminatorType.STRING)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //Least normalisation strategy
public abstract class Solicitacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
        name = "solicitacao_trechos", 
        joinColumns = {
            @JoinColumn(name = "solicitacao_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "trechos_id", referencedColumnName = "id")
        }
    )
    @OrderBy(value = "data_saida")
    private final List<Trecho> trechos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EEstadoSolicitacao estadoSolicitacao = EEstadoSolicitacao.PREENCHENDO;

    @Enumerated(EnumType.STRING)
    private ETipoSolicitacao tipoSolicitacao;
    
    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy(value = "data")
    private List<Mensagem> mensagens = new ArrayList<>();
    
    private LocalDateTime enviadoUnidadeEm;
    
    private LocalDateTime dataAvaliacaoPelaDepartamento;
    
    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setMensagens(List<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }

    public Solicitacao() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Solicitacao(ETipoSolicitacao tipoSolicitacao) {
        this.tipoSolicitacao = tipoSolicitacao;
    }

    public EEstadoSolicitacao getEstadoSolicitacao() {
        return estadoSolicitacao;
    }

    public void setEstadoSolicitacao(EEstadoSolicitacao estadoSolicitacao) {
        this.estadoSolicitacao = estadoSolicitacao;
    }

    public ETipoSolicitacao getTipoSolicitacao() {
        return tipoSolicitacao;
    }

    public void setTipoSolicitacao(ETipoSolicitacao tipoSolicitacao) {
        this.tipoSolicitacao = tipoSolicitacao;
    }
    
    public Boolean isEmProcessamento() {
        return !this.isAprovada() && !this.isRecusada();
    }
    
    public Boolean isRecusada() {
        return this.getEstadoSolicitacao().equals(EEstadoSolicitacao.RECUSADA);
    }
    
    public Boolean isDiaria() {
        return this.getTipoSolicitacao().equals(ETipoSolicitacao.DIARIA);
    }

    public Boolean isPassagem() {
        return this.getTipoSolicitacao().equals(ETipoSolicitacao.PASSAGEM);
    }

    public Boolean isTransporte() {
        return this.getTipoSolicitacao().equals(ETipoSolicitacao.TRANSPORTE);
    }

    public Boolean isPreenchida() {
        return this.getEstadoSolicitacao().equals(EEstadoSolicitacao.PREENCHIDA);
    }

    public Boolean isPreenchendo() {
        return getEstadoSolicitacao().equals(EEstadoSolicitacao.PREENCHENDO);
    }

    public Boolean isSubmetida() {
        return getEstadoSolicitacao().equals(EEstadoSolicitacao.SUBMETIDA);
    }

    /**
     * Verifica se uma solicitação é ou não deletável.
     * 
     * A solicitação é somente deletável se seu estado é de SUBMETIDA ou PREENCHENDO.
     * 
     * @return true em caso afirmativo, false do contrário.
     */
    public Boolean isDeletavel() {
        return this.getEstadoSolicitacao() == EEstadoSolicitacao.SUBMETIDA ||  this.getEstadoSolicitacao() == EEstadoSolicitacao.PREENCHENDO;
    }
    
    /**
     * Verifica se a solicitação foi aprovada.
     * @return true em caso afirmativo, false do contrário.
     */
    public Boolean isAprovada() {
        return this.getEstadoSolicitacao().equals(EEstadoSolicitacao.APROVADA);
    }
    
    public Boolean isDevolvida() {
        return this.getEstadoSolicitacao().equals(EEstadoSolicitacao.DEVOLVIDA);
    }
    
    /**
     * Verifica se a solicitação está aprovada e se é do tipo diária.
     * @return  true em caso afirmativo, false do contrário.
     */
    public Boolean isDiariaAndAprovada() {
        
        boolean itsOk= this.isDiaria() && this.isAprovada();
        return itsOk;
    }
    
    /**
     * Verifica se a solicitação foi encerrada.
     * @return true em caso afirmativo, false do contrário.
     */
    public Boolean isEncerrada() {
        return getEstadoSolicitacao().equals(EEstadoSolicitacao.ENCERRADA);
    }

    @Override
    public String toString() {
        return "\n\t\t\tSolicitacao {" + "\n\t\tid=" + getId() + ", \n\t\ttrechos=" + trechos.toString() + ", \n\t\testadoSolicitacao=" + getEstadoSolicitacao() + ", \n\t\ttipoSolicitacao=" + getTipoSolicitacao() + ", \n\t\tmensagens=" + getMensagens() + "\n\t\t\t}";
    }

    /**
     * @return the trechosVolta
     */
    public List<Trecho> getTrechosVolta() {
       
        List<Trecho> list = new ArrayList<>();
        
        this.trechos.forEach((Trecho t) -> {
        
            if (t.isTrechoVolta()) {
                list.add(t);
            }
        });
        
        return list;
    }

    public List<Trecho> getTrechosIda() {

        List<Trecho> list = new ArrayList<>();
        
        this.trechos.forEach((Trecho t) -> {
        
            if (t.isTrechoIda()) {
                list.add(t);
            }
        });
        
        return list;
    }
    
    public void removerTrechos() {
        
        // this.getTrechosIda().clear();
        // this.getTrechosVolta().clear();
        
        this.trechos.clear();
    }
   
    public void addTrecho(Trecho trecho) {
        this.trechos.add(trecho);
    }

    /**
     * @return the enviadoUnidadeEm
     */
    public LocalDateTime getEnviadoUnidadeEm() {
        return enviadoUnidadeEm;
    }

    /**
     * @param enviadoUnidadeEm the enviadoUnidadeEm to set
     */
    public void setEnviadoUnidadeEm(LocalDateTime enviadoUnidadeEm) {
        this.enviadoUnidadeEm = enviadoUnidadeEm;
    }

    /**
     * @return the dataAvaliacaoPelaDepartamento
     */
    public LocalDateTime getDataAvaliacaoPelaDepartamento() {
        return dataAvaliacaoPelaDepartamento;
    }

    /**
     * @param dataAvaliacaoPelaDepartamento the dataAvaliacaoPelaDepartamento to set
     */
    public void setDataAvaliacaoPelaDepartamento(LocalDateTime dataAvaliacaoPelaDepartamento) {
        this.dataAvaliacaoPelaDepartamento = dataAvaliacaoPelaDepartamento;
    }
    
    public String tipoSolicitacao() {
        
        if (this.isDiaria()) {
            
            return "Diária";
            
        }
        else if (this.isPassagem()) {
            
            return "Passagem";
        }
       
        return "Transporte";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Solicitacao other = (Solicitacao) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
