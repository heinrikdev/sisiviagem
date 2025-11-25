package ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.VeiculoProprio;

/**
 *
 * @author Ronaldo N Sousa
 * Criado em: 22/07/2019
 */
@Entity
@Table(name = "termo_responsabilidade_veiculo_proprio")
public class TermoOpcaoCompromissoResponsabilidade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer identificador;
    private LocalDate dataEmissao;
    private Long codigoVerificacao;
    
    @OneToOne
    private VeiculoProprio veiculoProprio;

    public TermoOpcaoCompromissoResponsabilidade(Long id, Integer identificador, LocalDate dataEmissao, Long codigoVerificacao, VeiculoProprio veiculoProprio) {
        this.id = id;
        this.identificador = identificador;
        this.dataEmissao = dataEmissao;
        this.codigoVerificacao = codigoVerificacao;
        this.veiculoProprio = veiculoProprio;
    }

    public TermoOpcaoCompromissoResponsabilidade() {
    }
    
    

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

    public VeiculoProprio getVeiculoProprio() {
        return veiculoProprio;
    }

    public void setVeiculoProprio(VeiculoProprio veiculoProprio) {
        this.veiculoProprio = veiculoProprio;
    }
}
