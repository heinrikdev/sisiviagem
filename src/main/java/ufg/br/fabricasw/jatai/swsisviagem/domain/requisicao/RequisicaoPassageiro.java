package ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

import org.hibernate.Hibernate; // <<<<<<<<<<
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EInfoTrajetoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.ParamFieldName;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.SupportsCustomizedBinding;

@Entity
@Table(name = "requisicao_passageiros")
@SupportsCustomizedBinding
public class RequisicaoPassageiro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ParamFieldName("requisicao_passageiro_id")
    private Long id;

    // Em geral é N:1. Se no teu modelo for realmente 1:1, mantém OneToOne.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="passageiros_id", referencedColumnName = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Passageiro passageiro;

    // N:1 para a Requisicao (sem cascade pro pai!)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="requisicao_id", referencedColumnName = "id")
    @JsonIgnore
    private Requisicao requisicao;

    private String motivo;

    private EInfoTrajetoPassageiro infoTrajetoPassageiro;

    private EEstadoPassageiro estadoIda     = EEstadoPassageiro.Aguardando;

    private EEstadoPassageiro estadoVolta   = EEstadoPassageiro.Aguardando;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return this.passageiro != null ? this.passageiro.getNome() : null; }
    public String getIdentificacao() { return this.passageiro != null ? this.passageiro.getIdentificacao() : null; }
    public String getStatusIda() { return this.estadoIda.toString(); }
    public String getStatusVolta() { return this.estadoVolta.toString(); }
    public String getEstadoPassageiro() {
        return "IDA: " + this.getEstadoIda() + " VOLTA: " + this.getEstadoVolta();
    }

    public Passageiro getPassageiro() { return passageiro; }
    public void setPassageiro(Passageiro passageiro) { this.passageiro = passageiro; }

    public EInfoTrajetoPassageiro getInfoTrajetoPassageiro() { return infoTrajetoPassageiro; }
    public void setInfoTrajetoPassageiro(EInfoTrajetoPassageiro v) { this.infoTrajetoPassageiro = v; }

    public Boolean isTrajetoSomenteIda()   { return this.infoTrajetoPassageiro == EInfoTrajetoPassageiro.Ida; }
    public Boolean isTrajetoSomenteVolta() { return this.infoTrajetoPassageiro == EInfoTrajetoPassageiro.Volta; }
    public Boolean isTrajetoIdaEVolta()    { return this.infoTrajetoPassageiro == EInfoTrajetoPassageiro.IdaEVolta; }
    public Boolean isTrajetoNaoInformado() { return this.infoTrajetoPassageiro == EInfoTrajetoPassageiro.NaoInformado; }

    public Requisicao getRequisicao() { return requisicao; }
    public void setRequisicao(Requisicao requisicao) { this.requisicao = requisicao; }

    public EEstadoPassageiro getEstadoIda() { return estadoIda; }
    public void setEstadoIda(EEstadoPassageiro estadoIda) { this.estadoIda = estadoIda; }

    public EEstadoPassageiro getEstadoVolta() { return estadoVolta; }
    public void setEstadoVolta(EEstadoPassageiro estadoVolta) { this.estadoVolta = estadoVolta; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getLabelColorInfoTrajeto() {
        String[] labels = { "label-primary", "label-info", "label-success", "label-danger" };
        return labels[this.getInfoTrajetoPassageiro().id()];
    }

    @Override
    public String toString() {
        return "\nRequisicaoPassageiro {"
                + "\n\tid=" + id
                + ",\n\tpassageiro=" + (passageiro != null ? passageiro.getId() : null)
                + ",\n\tinfoTrajetoPassageiro=" + infoTrajetoPassageiro
                + "\n}";
    }

    // ======== equals/hashCode à prova de proxy ========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RequisicaoPassageiro other = (RequisicaoPassageiro) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        // constante para estabilidade antes/depois do persist
        return 31;
    }
}