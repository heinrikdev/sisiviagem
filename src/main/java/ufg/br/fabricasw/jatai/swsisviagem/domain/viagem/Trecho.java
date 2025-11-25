package ufg.br.fabricasw.jatai.swsisviagem.domain.viagem;

import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoTransporte;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.ParamFieldName;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.SupportsCustomizedBinding;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.validators.CustomLocalDateNotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@SupportsCustomizedBinding
public class Trecho implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ParamFieldName("trecho_id")
    private Long id;

    @NotBlank(message = "Este campo é obrigatório.")
    private String cidadeOrigem;
    
    @NotBlank(message = "Este campo é obrigatório.")
    private String cidadeDestino;

    @NotBlank(message = "Este campo é obrigatório.")
    private String estadoOrigem;
    
    @NotBlank(message = "Este campo é obrigatório.")
    private String estadoDestino;

    private Boolean isOrigemDoBrasil = true;

    private Boolean isDestinoDoBrasil = true;

    @CustomLocalDateNotEmpty
    private LocalDate dataSaida;
    
    @CustomLocalDateNotEmpty
    private LocalDate dataChegada;

    private String horaChegada;
    
    private String horaSaida;
    
    @NotBlank(message = "Este campo é obrigatório.")
    private String tipoTransporte;
    
    @NotBlank(message = "Este campo é obrigatório.")
    private String tipoTrecho;
    
    public Trecho() {
        this.cidadeOrigem = "";
        this.cidadeDestino = "";
        this.estadoOrigem = "";
        this.estadoDestino = "";
    }

    public Trecho(Long id, String cidadeOrigem, String cidadeDestino, String estadoOrigem, String estadoDestino, LocalDate dataSaida, LocalDate dataChegada, String horaChegada, String horaSaida, String tipoTransporte, String tipoTrecho) {
        this.id = id;
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
        this.estadoOrigem = estadoOrigem;
        this.estadoDestino = estadoDestino;
        this.dataSaida = dataSaida;
        this.dataChegada = dataChegada;
        this.horaChegada = horaChegada;
        this.horaSaida = horaSaida;
        this.tipoTransporte = tipoTransporte;
        this.tipoTrecho = tipoTrecho;
    }
    
    public String getHoraChegada() {
        return horaChegada;
    }

    public void setHoraChegada(String horaChegada) {
        this.horaChegada = horaChegada;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public String getTipoTrecho() {
        return tipoTrecho;
    }

    public void setTipoTrecho(String tipoTrecho) {
        this.tipoTrecho = tipoTrecho;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCidadeOrigem() {
        return cidadeOrigem;
    }

    public void setCidadeOrigem(String cidadeOrigem) {
        this.cidadeOrigem = cidadeOrigem;
    }

    public String getCidadeDestino() {
        return cidadeDestino;
    }

    public void setCidadeDestino(String cidadeDestino) {
        this.cidadeDestino = cidadeDestino;
    }

    public String getEstadoOrigem() {
        return estadoOrigem;
    }

    public void setEstadoOrigem(String estadoOrigem) {
        this.estadoOrigem = estadoOrigem;
    }

    public String getEstadoDestino() {
        return estadoDestino;
    }

    public void setEstadoDestino(String estadoDestino) {
        this.estadoDestino = estadoDestino;
    }

    public LocalDate getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDate dataSaida) {
        this.dataSaida = dataSaida;
    }

    public LocalDate getDataChegada() {
        return dataChegada;
    }

    public void setDataChegada(LocalDate dataChegada) {
        this.dataChegada = dataChegada;
    }

    public String getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public Boolean getIsOrigemDoBrasil() {
        return isOrigemDoBrasil;
    }

    public void setIsOrigemDoBrasil(Boolean origemDoBrasil) {
        isOrigemDoBrasil = origemDoBrasil;
    }

    public Boolean getIsDestinoDoBrasil() {
        return isDestinoDoBrasil;
    }

    public void setIsDestinoDoBrasil(Boolean destinoDoBrasil) {
        isDestinoDoBrasil = destinoDoBrasil;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
 
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final Trecho other = (Trecho) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "\n\tTrecho {"
            + "\n\t\tid=" + id
            + ",\n\t\tcidadeOrigem='" + cidadeOrigem + '\''
            + ",\n\t\tcidadeDestino='" + cidadeDestino + '\''
            + ",\n\t\testadoOrigem='" + estadoOrigem + '\''
            + ",\n\t\testadoDestino='" + estadoDestino + '\''
            + ",\n\t\tdataSaida='" + dataSaida + '\''
            + ",\n\t\tdataChegada='" + dataChegada + '\''
            + ",\n\t\thoraChegada='" + horaChegada + '\''
            + ",\n\t\thoraSaida='" + horaSaida + '\''
            + ",\n\t\ttipoTransporte='" + tipoTransporte + '\''
            + ",\n\t\ttipoTrecho='" + tipoTrecho + '\''
        + "\n\t}";
    }
    
    public String getLabelColorInfoTrajeto() {
        return this.getTipoTrecho().equals("Ida") ? "label-primary": "label-info";
    };
    
    public Boolean isTrechoIda() {
        return this.tipoTrecho.equals("Ida");
    }
    
    public Boolean isTrechoVolta() {
        return this.tipoTrecho.equals("Volta");
    }
    
    public boolean isTransporteVeiculoProprio() {
        return this.tipoTransporte.equals(ETipoTransporte.VEICULO_PROPRIO.toString());
    }
    
    @Override
    public Trecho clone() {

        return new Trecho
        (
            null,
            this.getCidadeOrigem(),
            this.getCidadeDestino(),
            this.getEstadoOrigem(),
            this.getEstadoDestino(),
            this.getDataSaida(),
            this.getDataChegada(),
            this.getHoraChegada(),
            this.getHoraSaida(),
            this.getTipoTransporte(),
            this.getTipoTrecho()
        );
    }

    public Boolean isDeVeiculoProprio() {
        return this.tipoTransporte.equals(ETipoTransporte.VEICULO_PROPRIO.toString());
    }
}
