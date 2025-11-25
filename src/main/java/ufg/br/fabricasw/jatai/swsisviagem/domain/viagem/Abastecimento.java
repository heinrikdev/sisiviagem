package ufg.br.fabricasw.jatai.swsisviagem.domain.viagem;

import javax.persistence.*;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoCombustivel;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoServico;

@Entity
public class Abastecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Veiculo veiculo;

    private ETipoCombustivel tipoCombustivel;

    private ETipoServico tipoServico;

    private String observacao;

    private Integer odometro;

    private Double litros;

    private Double valor;

    private String idaVolta;

    public String getIdaVolta() {
        return idaVolta;
    }

    public void setIdaVolta(String idaVolta) {
        this.idaVolta = idaVolta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public ETipoCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(ETipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public ETipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(ETipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getOdometro() {
        return odometro;
    }

    public void setOdometro(Integer odometro) {
        this.odometro = odometro;
    }

    public Double getLitros() {
        return litros;
    }

    public void setLitros(Double litros) {
        this.litros = litros;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public int getTipoServicoId() {
        return this.tipoServico.id();
    }

    public int getTipoCombustivelId() {
        return this.tipoCombustivel.id();
    }

    public String getPlacaVeiculo() {
        return this.veiculo.getPlaca();
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Abastecimento that = (Abastecimento) o;
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (veiculo != null ? veiculo.hashCode() : 0);
        result = 31 * result + (tipoCombustivel != null ? tipoCombustivel.hashCode() : 0);
        result = 31 * result + (tipoServico != null ? tipoServico.hashCode() : 0);
        result = 31 * result + (observacao != null ? observacao.hashCode() : 0);
        result = 31 * result + (odometro != null ? odometro.hashCode() : 0);
        result = 31 * result + (litros != null ? litros.hashCode() : 0);
        result = 31 * result + (valor != null ? valor.hashCode() : 0);
        result = 31 * result + (idaVolta != null ? idaVolta.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Abastecimento{"
                + "id=" + id
                + ", veiculo=" + veiculo
                + ", tipoCombustivel=" + tipoCombustivel
                + ", tipoServico=" + tipoServico
                + ", observacao='" + observacao + '\''
                + ", odometro=" + odometro
                + ", litros=" + litros
                + ", valor=" + valor
                + ", idaVolta='" + idaVolta + '\''
                + '}';
    }
}
