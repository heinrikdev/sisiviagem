package ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.devolucao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Ronaldo N Sousa
 * Criado em: Dec 20, 2018
 */
@Entity
public class DevolucaoDeRequisicao implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime enviadoEm = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    
    private String devolvidoPor;
    
    private String descricao;
    
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
     * @return the enviadoEm
     */
    public LocalDateTime getEnviadoEm() {
        return enviadoEm;
    }

    /**
     * @param enviadoEm the enviadoEm to set
     */
    public void setEnviadoEm(LocalDateTime enviadoEm) {
        this.enviadoEm = enviadoEm;
    }

    /**
     * @return the devolvidoPor
     */
    public String getDevolvidoPor() {
        return devolvidoPor;
    }

    /**
     * @param devolvidoPor the devolvidoPor to set
     */
    public void setDevolvidoPor(String devolvidoPor) {
        this.devolvidoPor = devolvidoPor;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "DescricaoDevolucao{" + "id=" + id + ", enviadoEm=" + enviadoEm + ", devolvidoPor=" + devolvidoPor + ", descricao=" + descricao + '}';
    }
}
