package ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * Ao prestar contas o lançador inseri as informçãoes da requisição no SCDP que
 * é um sistema do governo. Um número é gerado so sistema como forma de
 * identificá-lo e há valores de passagem e diária (gastos).
 *
 * @author ronogue
 */
@Entity
public class InfoRequisicaoScdp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String numeroScdp;
    
    private Double valorDiaria;
    
    private Double valorPassagem;
    
    private LocalDateTime inseridoEm = LocalDateTime.now();

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

    /**
     * @return the numeroScdp
     */
    public String getNumeroScdp() {
        return numeroScdp;
    }

    /**
     * @param numeroScdp the numeroScdp to set
     */
    public void setNumeroScdp(String numeroScdp) {
        this.numeroScdp = numeroScdp;
    }

    /**
     * @return the valorDiaria
     */
    public Double getValorDiaria() {
        return valorDiaria;
    }

    /**
     * @param valorDiaria the valorDiaria to set
     */
    public void setValorDiaria(Double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    /**
     * @return the valorPassagem
     */
    public Double getValorPassagem() {
        return valorPassagem;
    }

    /**
     * @param valorPassagem the valorPassagem to set
     */
    public void setValorPassagem(Double valorPassagem) {
        this.valorPassagem = valorPassagem;
    }

    @Override
    public String toString() {
        return "InfoRequisicaoScdp{" + "id=" + id + ", numeroScdp=" + numeroScdp + ", valorDiaria=" + valorDiaria + ", valorPassagem=" + valorPassagem + '}';
    }

    /**
     * @return the inseridoEm
     */
    public LocalDateTime getInseridoEm() {
        return inseridoEm;
    }

    /**
     * @param inseridoEm the inseridoEm to set
     */
    public void setInseridoEm(LocalDateTime inseridoEm) {
        this.inseridoEm = inseridoEm;
    }
}
