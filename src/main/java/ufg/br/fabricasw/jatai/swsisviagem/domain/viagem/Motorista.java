package ufg.br.fabricasw.jatai.swsisviagem.domain.viagem;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.ParamFieldName;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.SupportsCustomizedBinding;

@Entity
@SupportsCustomizedBinding
public class Motorista implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ParamFieldName("motorista_id")
    private Long id;

    private String habilitacao;
    private String credencial;
    private LocalDate dataExpedicao;
    private LocalDate dataValidade;
    private Boolean isAtivo = true;

    @OneToOne(cascade =  CascadeType.ALL, optional = false)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHabilitacao() {
        return habilitacao;
    }

    public void setHabilitacao(String habilitacao) {
        this.habilitacao = habilitacao;
    }

    public String getCredencial() {
        return credencial;
    }

    public void setCredencial(String credencial) {
        this.credencial = credencial;
    }

    public LocalDate getDataExpedicao() {
        return dataExpedicao;
    }

    public void setDataExpedicao(LocalDate dataExpedicao) {
        this.dataExpedicao = dataExpedicao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    public String getNome() {
        return this.pessoa.getNome();
    }
    
    public String getEmail() {
        return this.pessoa.getEmail();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Motorista motorista = (Motorista) o;

        if (id != null ? !id.equals(motorista.id) : motorista.id != null) {
            return false;
        }
        if (habilitacao != null ? !habilitacao.equals(motorista.habilitacao) : motorista.habilitacao != null) {
            return false;
        }
        if (credencial != null ? !credencial.equals(motorista.credencial) : motorista.credencial != null) {
            return false;
        }
        if (dataExpedicao != null ? !dataExpedicao.equals(motorista.dataExpedicao) : motorista.dataExpedicao != null) {
            return false;
        }
        if (dataValidade != null ? !dataValidade.equals(motorista.dataValidade) : motorista.dataValidade != null) {
            return false;
        }
        return pessoa != null ? pessoa.equals(motorista.pessoa) : motorista.pessoa == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (habilitacao != null ? habilitacao.hashCode() : 0);
        result = 31 * result + (credencial != null ? credencial.hashCode() : 0);
        result = 31 * result + (dataExpedicao != null ? dataExpedicao.hashCode() : 0);
        result = 31 * result + (dataValidade != null ? dataValidade.hashCode() : 0);
        result = 31 * result + (pessoa != null ? pessoa.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return 
        "\n\tMotorista {"
            + "\n\t\tid=" + id
            + ",\n\t\thabilitacao='" + habilitacao + '\''
            + ",\n\t\tcredencial='" + credencial + '\''
            + ",\n\t\tdataExpedicao='" + dataExpedicao + '\''
            + ",\n\t\tdataValidade='" + dataValidade + '\''
            + ",pessoa=" + pessoa
        + "\n\t\t}";
    }

    /**
     * @return the isAtivo
     */
    public Boolean getIsAtivo() {
        return isAtivo;
    }

    /**
     * @param isAtivo the isAtivo to set
     */
    public void setIsAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }
}
