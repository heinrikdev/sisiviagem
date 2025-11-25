package ufg.br.fabricasw.jatai.swsisviagem.domain.viagem;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
public class ViagemRelatorio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer identificador;
    private LocalDate dataEmissao;
    private Long codigoVerificacao;

    @OneToOne
    private Viagem viagem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(LocalDate dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Long getCodigoVerificacao() {
        return codigoVerificacao;
    }

    public void setCodigoVerificacao(Long codigoVerificacao) {
        this.codigoVerificacao = codigoVerificacao;
    }

    public Viagem getViagem() {
        return viagem;
    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ViagemRelatorio that = (ViagemRelatorio) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
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
        return viagem != null ? viagem.equals(that.viagem) : that.viagem == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (identificador != null ? identificador.hashCode() : 0);
        result = 31 * result + (dataEmissao != null ? dataEmissao.hashCode() : 0);
        result = 31 * result + (codigoVerificacao != null ? codigoVerificacao.hashCode() : 0);
        result = 31 * result + (viagem != null ? viagem.hashCode() : 0);
        return result;
    }
    
    public LocalDate getDataInicioViagem() {
        return this.viagem.getData();
    }
    
    public LocalDate getDataTerminoViagem() {
        return this.viagem.getDataTermino();
    }

    @Override
    public String toString() {
        return "ViagemRelatorio{"
                + "id=" + id
                + ", identificador=" + identificador
                + ", dataEmissao='" + dataEmissao + '\''
                + ", codigoVerificacao=" + codigoVerificacao
                + ", viagem=" + viagem
                + '}';
    }
}
