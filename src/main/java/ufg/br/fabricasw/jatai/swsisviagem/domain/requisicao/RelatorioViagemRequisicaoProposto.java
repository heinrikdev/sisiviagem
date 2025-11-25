package ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Ronaldo N Sousa
 * Criado em: 22/07/2019
 */
@Entity
@Table(name = "relatorio_viagem_requisicao_proposto")
public class RelatorioViagemRequisicaoProposto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer identificador;
    private LocalDate dataEmissao;
    private Long codigoVerificacao;
    
    @OneToOne(mappedBy = "relatorioViagemProposto")
    private Requisicao requisicao;

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

    public Requisicao getRequisicao() {
        return requisicao;
    }

    public void setRequisicao(Requisicao requisicao) {
        this.requisicao = requisicao;
    }
}
