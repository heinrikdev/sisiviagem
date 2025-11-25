package ufg.br.fabricasw.jatai.swsisviagem.domain.viagem;

import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ComprovanteViagem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer identificador;
    private String dataEmissao;
    private Long codigoVerificacao;
    @OneToOne
    private Requisicao requisicao;

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

        ComprovanteViagem that = (ComprovanteViagem) o;

        if (getId() != null ? !id.equals(that.id) : that.getId() != null) {
            return false;
        }
        if (getIdentificador() != null ? !identificador.equals(that.identificador) : that.getIdentificador() != null) {
            return false;
        }
        if (getDataEmissao() != null ? !dataEmissao.equals(that.dataEmissao) : that.getDataEmissao() != null) {
            return false;
        }
        if (getCodigoVerificacao() != null ? !codigoVerificacao.equals(that.codigoVerificacao) : that.getCodigoVerificacao() != null) {
            return false;
        }
        return getRequisicao() != null ? getRequisicao().equals(that.getRequisicao()) : that.getRequisicao() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getIdentificador() != null ? getIdentificador().hashCode() : 0);
        result = 31 * result + (getDataEmissao() != null ? getDataEmissao().hashCode() : 0);
        result = 31 * result + (getCodigoVerificacao() != null ? getCodigoVerificacao().hashCode() : 0);
        result = 31 * result + (getRequisicao() != null ? getRequisicao().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComprovanteViagem{"
                + "id=" + getId()
                + ", identificador=" + getIdentificador()
                + ", dataEmissao='" + getDataEmissao() + '\''
                + ", codigoVerificacao=" + getCodigoVerificacao()
                + ", requisicao=" + getRequisicao()
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
