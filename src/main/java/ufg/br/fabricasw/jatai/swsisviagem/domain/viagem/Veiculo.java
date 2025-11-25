package ufg.br.fabricasw.jatai.swsisviagem.domain.viagem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by guilherme on 24/11/17.
 */
@Entity
public class Veiculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String carro;
    private String placa;
    private String combustivel;
    private String marca;
    private String modelo;
    private String ano;

    private String chassis;
    private String renavan;
    private Integer arCondicionado;
    private Integer direcaoHidraulica;
    private String pneus;
    private Integer capacidade;
    private Boolean isAtivo = true;

    public Long getId() {
        return id;
    }
    
    public Boolean hasArcondicionado() {
        return this.arCondicionado > 0;
    }
    
    public Boolean hasDirecaoHidraulica() {
        return this.direcaoHidraulica > 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarro() {
        return carro;
    }

    public void setCarro(String carro) {
        this.carro = carro;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
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

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public String getRenavan() {
        return renavan;
    }

    public void setRenavan(String renavan) {
        this.renavan = renavan;
    }

    public Integer getArCondicionado() {
        return arCondicionado;
    }

    public void setArCondicionado(Integer arCondicionado) {
        this.arCondicionado = arCondicionado;
    }

    public Integer getDirecaoHidraulica() {
        return direcaoHidraulica;
    }

    public void setDirecaoHidraulica(Integer direcaoHidraulica) {
        this.direcaoHidraulica = direcaoHidraulica;
    }

    public String getPneus() {
        return pneus;
    }

    public void setPneus(String pneus) {
        this.pneus = pneus;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Veiculo veiculo = (Veiculo) o;

        if (id != null ? !id.equals(veiculo.id) : veiculo.id != null) {
            return false;
        }
        if (carro != null ? !carro.equals(veiculo.carro) : veiculo.carro != null) {
            return false;
        }
        if (placa != null ? !placa.equals(veiculo.placa) : veiculo.placa != null) {
            return false;
        }
        if (combustivel != null ? !combustivel.equals(veiculo.combustivel) : veiculo.combustivel != null) {
            return false;
        }
        if (marca != null ? !marca.equals(veiculo.marca) : veiculo.marca != null) {
            return false;
        }
        if (modelo != null ? !modelo.equals(veiculo.modelo) : veiculo.modelo != null) {
            return false;
        }
        if (ano != null ? !ano.equals(veiculo.ano) : veiculo.ano != null) {
            return false;
        }
        if (chassis != null ? !chassis.equals(veiculo.chassis) : veiculo.chassis != null) {
            return false;
        }
        if (renavan != null ? !renavan.equals(veiculo.renavan) : veiculo.renavan != null) {
            return false;
        }
        if (arCondicionado != null ? !arCondicionado.equals(veiculo.arCondicionado) : veiculo.arCondicionado != null) {
            return false;
        }
        if (direcaoHidraulica != null ? !direcaoHidraulica.equals(veiculo.direcaoHidraulica) : veiculo.direcaoHidraulica != null) {
            return false;
        }
        if (pneus != null ? !pneus.equals(veiculo.pneus) : veiculo.pneus != null) {
            return false;
        }
        return capacidade != null ? capacidade.equals(veiculo.capacidade) : veiculo.capacidade == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (carro != null ? carro.hashCode() : 0);
        result = 31 * result + (placa != null ? placa.hashCode() : 0);
        result = 31 * result + (combustivel != null ? combustivel.hashCode() : 0);
        result = 31 * result + (marca != null ? marca.hashCode() : 0);
        result = 31 * result + (modelo != null ? modelo.hashCode() : 0);
        result = 31 * result + (ano != null ? ano.hashCode() : 0);
        result = 31 * result + (chassis != null ? chassis.hashCode() : 0);
        result = 31 * result + (renavan != null ? renavan.hashCode() : 0);
        result = 31 * result + (arCondicionado != null ? arCondicionado.hashCode() : 0);
        result = 31 * result + (direcaoHidraulica != null ? direcaoHidraulica.hashCode() : 0);
        result = 31 * result + (pneus != null ? pneus.hashCode() : 0);
        result = 31 * result + (capacidade != null ? capacidade.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return 
        "\n\t\tVeiculo {"
            + "\n\t\t\tid=" + id
            + ",\n\t\t\tcarro='" + carro + '\''
            + ",\n\t\t\tplaca='" + placa + '\''
            + ",\n\t\t\tcombustivel='" + combustivel + '\''
            + ",\n\t\t\tmarca='" + marca + '\''
            + ",\n\t\t\tmodelo='" + modelo + '\''
            + ",\n\t\t\tano='" + ano + '\''
            + ",\n\t\t\tchassis='" + chassis + '\''
            + ",\n\t\t\trenavan='" + renavan + '\''
            + ",\n\t\t\tarCondicionado=" + arCondicionado
            + ",\n\t\t\tdirecaoHidraulica=" + direcaoHidraulica
            + ",\n\t\t\tpneus='" + pneus + '\''
            + ",\n\t\t\tcapacidade=" + capacidade
        + "\n\t\t}";
    }

    /**
     * @return the isAtivo
     */
    public Boolean getIsAtivo() {
        return isAtivo;
    }

    /**
     * @param isAtivo the isAtivo to set
     */
    public void setIsAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }
}
