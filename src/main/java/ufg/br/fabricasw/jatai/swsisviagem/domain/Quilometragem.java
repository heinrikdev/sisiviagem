package ufg.br.fabricasw.jatai.swsisviagem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Veiculo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoQuilometragem;

@Entity
public class Quilometragem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Viagem viagem;

    @ManyToOne
    @JsonIgnore
    private Veiculo veiculo;
    
    private ETipoQuilometragem tipoQuilometragem;

    private Double quilometro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Viagem getViagem() {
        return viagem;
    }

    public void setViagem(Viagem viagem) {
        this.viagem = viagem;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Double getQuilometro() {
        return quilometro;
    }

    public void setQuilometro(Double quilometro) {
        this.quilometro = quilometro;
    }
    
    public String getVeiculoInfo() {
        return this.veiculo.getPlaca() + " - " + this.veiculo.getModelo();
    }
            
    public String getDescricaoVeiculo(){
        return this.veiculo.getPlaca() + ", " + this.veiculo.getModelo();
    }
    
    public Long getVeiculoId() {
        return this.veiculo.getId();
    }
    
    public Long getViagemId() {
        return this.viagem.getId();
    }

    @Override
    public String toString() {
        return "Quilometragem{"
                + "id=" + id
                + ", viagem=" + viagem
                + ", veiculo=" + veiculo
                + ", quilometro=" + quilometro
                + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.viagem);
        hash = 53 * hash + Objects.hashCode(this.veiculo);
        hash = 53 * hash + Objects.hashCode(this.tipoQuilometragem);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        
        final Quilometragem other = (Quilometragem) obj;
        
        if (this.id.equals(other.id)) {
            return true;
        }
        
        if (!Objects.equals(this.veiculo, other.veiculo)) {
            return false;
        }
        
        return this.tipoQuilometragem == other.tipoQuilometragem;
    }

    public ETipoQuilometragem getTipoQuilometragem() {
        return tipoQuilometragem;
    }

    public void setTipoQuilometragem(ETipoQuilometragem tipoQuilometragem) {
        this.tipoQuilometragem = tipoQuilometragem;
    }
}
