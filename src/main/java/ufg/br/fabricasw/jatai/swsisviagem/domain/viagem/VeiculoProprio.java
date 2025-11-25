package ufg.br.fabricasw.jatai.swsisviagem.domain.viagem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.TermoOpcaoCompromissoResponsabilidade;

@Entity
public class VeiculoProprio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    List<Trecho> trechos;
    
    @OneToOne(mappedBy = "documentoVeiculoProprio")
    @JsonIgnore
    private Requisicao requisicao;

    @OneToOne(mappedBy = "veiculoProprio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private TermoOpcaoCompromissoResponsabilidade termoResponsabilidade;

    private String marca;
    private String modelo;

    private String cor;

    private Integer ano;

    private String placa;

    public VeiculoProprio() {
        this.trechos = new ArrayList<>();
    }
    
    public List<Trecho> getTrechos() {
        return trechos;
    }

    public void setTrechos(List<Trecho> trechos) {
        this.trechos = trechos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VeiculoProprio that = (VeiculoProprio) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (trechos != null ? !trechos.equals(that.trechos) : that.trechos != null) {
            return false;
        }
        if (marca != null ? !marca.equals(that.marca) : that.marca != null) {
            return false;
        }
        if (modelo != null ? !modelo.equals(that.modelo) : that.modelo != null) {
            return false;
        }
        if (cor != null ? !cor.equals(that.cor) : that.cor != null) {
            return false;
        }
        if (ano != null ? !ano.equals(that.ano) : that.ano != null) {
            return false;
        }
        return placa != null ? placa.equals(that.placa) : that.placa == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (trechos != null ? trechos.hashCode() : 0);
        result = 31 * result + (marca != null ? marca.hashCode() : 0);
        result = 31 * result + (modelo != null ? modelo.hashCode() : 0);
        result = 31 * result + (cor != null ? cor.hashCode() : 0);
        result = 31 * result + (ano != null ? ano.hashCode() : 0);
        result = 31 * result + (placa != null ? placa.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VeiculoProprio {"
                + "id=" + id
                + ", trechos=" + trechos
                + ", marca='" + marca + '\''
                + ", modelo='" + modelo + '\''
                + ", cor='" + cor + '\''
                + ", ano=" + ano
                + ", placa='" + placa + '\''
                + '}';
    }
    
    public TermoOpcaoCompromissoResponsabilidade getTermoResponsabilidade() {
        return termoResponsabilidade;
    }

    public void setTermoResponsabilidade(TermoOpcaoCompromissoResponsabilidade termoResponsabilidade) {
        this.termoResponsabilidade = termoResponsabilidade;
    }
    
    public boolean hasTermoResponsabilidade() {
        return this.termoResponsabilidade != null;
    }

    /**
     * @return the requisicao
     */
    public Requisicao getRequisicao() {
        return requisicao;
    }

    /**
     * @param requisicao the requisicao to set
     */
    public void setRequisicao(Requisicao requisicao) {
        this.requisicao = requisicao;
    }
    
}
