package ufg.br.fabricasw.jatai.swsisviagem.domain.viagem;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.ParamFieldName;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.SupportsCustomizedBinding;

@Entity
@SupportsCustomizedBinding
public class Passageiro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ParamFieldName("passageiro_id")
    private Long id;

    @NotBlank(message = "Este campo é obrigatório.")
    @ParamFieldName("passageiro_nome")
    private String nome;

    @NotBlank(message = "Este campo é obrigatório.")
    private String identificacao;

    @NotBlank(message = "Este campo é obrigatório.")
    private String tipoSanguineo;

    @NotBlank(message = "Este campo é obrigatório.")
    private String rg;

    @NotBlank(message = "Este campo é obrigatório.")
    private String orgao = "";

    @NotBlank(message = "Este campo é obrigatório.")
    private String telefone;

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getOrgao() {
        return (orgao != null) ? orgao.toUpperCase() : "";
    }

    public void setOrgao(String orgao) {
        this.orgao = orgao;
    }

    public String getTelefone() {
        return (telefone != null) ? telefone : "Não informado.";
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return (nome != null) ? nome.toUpperCase() : "";
    }

    public void setNome(String nome) {
        
        if (nome.endsWith(",")) {
            
            this.nome = nome.substring(0, nome.length() - 1);
            
        } else if (nome.startsWith(",")) {
            
            this.nome = nome.substring(1, nome.length());
            
        } else {
            
            this.nome = nome;
        }
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Passageiro that = (Passageiro) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (nome != null ? !nome.equals(that.nome) : that.nome != null) {
            return false;
        }
        if (identificacao != null ? !identificacao.equals(that.identificacao) : that.identificacao != null) {
            return false;
        }
        if (tipoSanguineo != null ? !tipoSanguineo.equals(that.tipoSanguineo) : that.tipoSanguineo != null) {
            return false;
        }
        if (rg != null ? !rg.equals(that.rg) : that.rg != null) {
            return false;
        }
        if (orgao != null ? !orgao.equals(that.orgao) : that.orgao != null) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (identificacao != null ? identificacao.hashCode() : 0);
        result = 31 * result + (tipoSanguineo != null ? tipoSanguineo.hashCode() : 0);
        result = 31 * result + (rg != null ? rg.hashCode() : 0);
        result = 31 * result + (orgao != null ? orgao.hashCode() : 0);
        result = 31 * result + (telefone != null ? telefone.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n\tPassageiro {"
            + "\n\t\tid=" + id
            + ",\n\t\tnome='" + nome + '\''
            + ",\n\t\tIdentificacao='" + identificacao + '\''
            + ",\n\t\ttipoSanguineo='" + tipoSanguineo + '\''
            + ",\n\t\trg='" + rg + '\''
            + ",\n\t\torgao='" + orgao + '\''
            + ",\n\t\ttelefone='" + telefone + '\''
        + "\n\t}";
    }
}
