package ufg.br.fabricasw.jatai.swsisviagem.domain;

import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import javax.persistence.*;

@Entity
public class Justificativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motivo;
    private String pessoa;

    private Integer identificador;
    private String dataEmissao;
    private Long codigoVerificacao;
    private String dataFull;

    @OneToOne
    private Requisicao requisicao;

    public String getDataFull() {
        return dataFull;
    }

    public void setDataFull(String dataFull) {
        this.dataFull = dataFull;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getPessoa() {
        return pessoa;
    }

    public void setPessoa(String pessoa) {
        this.pessoa = pessoa;
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Long getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public void setCodigoVerificacao(Long codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public Requisicao getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(Requisicao requisicao) {
        this.requisicao = requisicao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Justificativa that = (Justificativa) o;

        if (getId() != null ? !id.equals(that.id) : that.getId() != null) {
            return false;
        }
        if (motivo != null ? !motivo.equals(that.motivo) : that.motivo != null) {
            return false;
        }
        if (pessoa != null ? !pessoa.equals(that.pessoa) : that.pessoa != null) {
            return false;
        }
        if (identificador != null ? !identificador.equals(that.identificador) : that.identificador != null) {
            return false;
        }
        if (dataEmissao != null ? !dataEmissao.equals(that.dataEmissao) : that.dataEmissao != null) {
            return false;
        }
        if (codigoVerificacao != null ? !codigoVerificacao.equals(that.codigoVerificacao) : that.codigoVerificacao != null) {
            return false;
        }
        if (dataFull != null ? !dataFull.equals(that.dataFull) : that.dataFull != null) {
            return false;
        }
        return requisicao != null ? requisicao.equals(that.requisicao) : that.requisicao == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (motivo != null ? motivo.hashCode() : 0);
        result = 31 * result + (pessoa != null ? pessoa.hashCode() : 0);
        result = 31 * result + (identificador != null ? identificador.hashCode() : 0);
        result = 31 * result + (dataEmissao != null ? dataEmissao.hashCode() : 0);
        result = 31 * result + (codigoVerificacao != null ? codigoVerificacao.hashCode() : 0);
        result = 31 * result + (dataFull != null ? dataFull.hashCode() : 0);
        result = 31 * result + (requisicao != null ? requisicao.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Justificativa{"
                + "id=" + getId()
                + ", motivo='" + motivo + '\''
                + ", pessoa='" + pessoa + '\''
                + ", identificador=" + identificador
                + ", dataEmissao='" + dataEmissao + '\''
                + ", codigoVerificacao=" + codigoVerificacao
                + ", dataFull='" + dataFull + '\''
                + ", requisicao=" + requisicao
                + '}';
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
}
