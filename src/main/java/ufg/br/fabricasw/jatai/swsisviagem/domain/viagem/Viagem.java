package ufg.br.fabricasw.jatai.swsisviagem.domain.viagem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoViagem;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import org.hibernate.annotations.Where;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Quilometragem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoQuilometragem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RequisicaoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.ParamFieldName;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.SupportsCustomizedBinding;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.json.LocalDateSerializer;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.validators.CustomLocalDateNotEmpty;

@Entity
@SupportsCustomizedBinding
public class Viagem implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ParamFieldName("viagem_id")
    private Long id;

    @NotBlank(message = "Este campo é obrigatório")
    @ParamFieldName("viagem_titulo")
    private String titulo;
    
    @NotBlank(message = "Este campo é obrigatório")
    @ParamFieldName("viagem_descricao")
    private String descricao = "";
    
    @CustomLocalDateNotEmpty
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate data;
    
    @NotBlank(message = "Este campo é obrigatório")
    private String startHora;
    
    @NotBlank(message = "Este campo é obrigatório")
    private String endHora;
    
    @CustomLocalDateNotEmpty
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dataTermino;

    private EEstadoViagem estadoViagem = EEstadoViagem.Aberta;

    @ManyToMany(mappedBy = "viagens")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private List<Requisicao> requisicaos = new ArrayList<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Anexo> anexos = new ArrayList<>();

    private String observacao = null;
    
    private boolean veiculoOkay;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Abastecimento> abastecimentos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tarefa> tarefas = new ArrayList<>();
    
    @ManyToMany
    private List<Veiculo> veiculos = new ArrayList<>();

    @ManyToMany
    private List<Motorista> motoristas = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "viagem_id", referencedColumnName = "id" )
    @Where(clause = "tipo_quilometragem = 0")
    private List<Quilometragem> quilometragemInicial;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "viagem_id", referencedColumnName = "id" )
    @Where(clause = "tipo_quilometragem = 1")
    private List<Quilometragem> quilometragemFinal;
    
    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    public List<Anexo> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<Anexo> anexos) {
        this.anexos = anexos;
    }

    public String getObservacao() {
        return (observacao != null) ? observacao : "---";
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<Abastecimento> getAbastecimentos() {
        return abastecimentos;
    }

    public void setAbastecimentos(List<Abastecimento> abastecimentos) {
        this.abastecimentos = abastecimentos;
    }

    public boolean isVeiculoOkay() {
        return veiculoOkay;
    }

    public void setVeiculoOkay(boolean veiculoOkay) {
        this.veiculoOkay = veiculoOkay;
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void setTarefas(List<Tarefa> tarefas) {
        this.tarefas = tarefas;
    }

    public EEstadoViagem getEstadoViagem() {
        return estadoViagem;
    }

    public void setEstadoViagem(EEstadoViagem estadoViagem) {
        this.estadoViagem = estadoViagem;
    }

    public List<Motorista> getMotoristas() {
        return motoristas;
    }

    public void setMotoristas(List<Motorista> motoristas) {
        this.motoristas = motoristas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getStartHora() {
        return startHora;
    }

    public void setStartHora(String startHora) {
        this.startHora = startHora;
    }

    public String getEndHora() {
        return endHora;
    }

    public void setEndHora(String endHora) {
        this.endHora = endHora;
    }

    public List<Requisicao> getRequisicaos() {
        return requisicaos;
    }

    public void setRequisicaos(List<Requisicao> requisicaos) {
        this.requisicaos = requisicaos;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculo) {
        this.veiculos = veiculo;
    }
    
    public void removerVeiculoViagem(Veiculo veiculo) {
        this.veiculos.remove(veiculo);
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
        
        final Viagem other = (Viagem) obj;
        return Objects.equals(this.id, other.id);
    }

    

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (titulo != null ? titulo.hashCode() : 0);
        result = 31 * result + (descricao != null ? descricao.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (startHora != null ? startHora.hashCode() : 0);
        result = 31 * result + (endHora != null ? endHora.hashCode() : 0);
        result = 31 * result + (dataTermino != null ? dataTermino.hashCode() : 0);
        result = 31 * result + (estadoViagem != null ? estadoViagem.hashCode() : 0);
        result = 31 * result + (anexos != null ? anexos.hashCode() : 0);
        result = 31 * result + (observacao != null ? observacao.hashCode() : 0);
        result = 31 * result + (abastecimentos != null ? abastecimentos.hashCode() : 0);
        result = 31 * result + (veiculoOkay ? 1 : 0);
        result = 31 * result + (tarefas != null ? tarefas.hashCode() : 0);
        result = 31 * result + (motoristas != null ? motoristas.hashCode() : 0);
        result = 31 * result + (requisicaos != null ? requisicaos.hashCode() : 0);
        result = 31 * result + (veiculos != null ? veiculos.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n\t\tViagem {"
            + "\n\t\t\tid=" + id
            + ",\n\t\t\ttitulo='" + titulo + '\''
            + ",\n\t\t\tdescricao='" + descricao + '\''
            + ",\n\t\t\tdata='" + data + '\''
            + ",\n\t\t\tstartHora='" + startHora + '\''
            + ",\n\t\t\tendHora='" + endHora + '\''
            + ",\n\t\t\tdataTermino='" + dataTermino + '\''
            + ",\n\t\t\testadoViagem=" + estadoViagem
            + ",\n\t\t\tanexos=" + anexos
            + ",\n\t\t\tobservacao='" + observacao + '\''
            + ",\n\t\t\tabastecimentos=" + abastecimentos
            + ",\n\t\t\tveiculoOkay=" + veiculoOkay
            + ",\n\t\t\ttarefas=" + tarefas
            + ",\n\t\t\tmotoristas=" + motoristas
            + ",\n\t\t\trequisicaos=" + requisicaos
            + ",\n\t\t\tveiculos=" + veiculos
        +"\n\t\t}";
    }
    
    /**
     * Verifica se a viagem possui observação.
     * @return true em caso afirmativo, false do contrário.
     */
    public Boolean hasObservacao() {
        return this.observacao != null;
    }
    
    /**
     * Verifica se o estado da viagem é encerrado.
     * @return true em caso afirmativo, false do contrário.
     */
    public Boolean isEncerrada() {
        return this.estadoViagem == EEstadoViagem.Encerrada;
    }
    
    /**
     * Verifica se o estado da viagem é finalizada.
     * @return true em caso afirmativo, false do contrário.
     */
    public Boolean isFinalizada() {
        return this.estadoViagem == EEstadoViagem.Finalizada;
    }
    
    /**
     * Verifica se a viagem está em andamento.
     * @return true em caso afirmativo, false do contrário.
     */
    public Boolean isEmAdamento() {
        return this.estadoViagem == EEstadoViagem.Em_andamento;
    }
    
    /**
     * Verifica se a viagem está aberta.
     * @return true em caso afirmativo, false do contrário
     */
    public Boolean isAberta() {
        return this.estadoViagem == EEstadoViagem.Aberta;
    }
    
    public Boolean isViagemCadastrada() {
        return this.getId() != null;
    }
    
    public List<RequisicaoPassageiro> getRequisicaoPassageiros() {
        
        List<RequisicaoPassageiro> passageiros = new ArrayList<>();
        
        this.requisicaos.stream().forEach((Requisicao req) -> {
            passageiros.addAll(req.getRequisicaoPassageiros());
        });
        
        return passageiros;
    }
    
    @JsonProperty("start")
    public String getStart() {
        return this.getData().toString() + "T" + this.getStartHora();
    }
    
    /**
     * O dia de termino é somado mais 1 pois o fullcalendar desconsidera o ultimo dia.
     * @return 
     */
    public String getEnd() {
        
        return this.getDataTermino().toString() + "T" + this.getEndHora();
    }
    
    @JsonProperty("color")
    public String getColor() {
        
        String[] colorsByEstadoViagem = {
            "#337ab7", // ENCERRADO
            "#28a745", // FINALIZADO
            "#f39c12", // ABERTO #17a2b8
            "#dc3545", // EM_ANDAMENTO
        };
        
        return colorsByEstadoViagem[this.getEstadoViagem().id()];
    }
    
    @JsonProperty("title")
    public String getTitle() {
        return " (" + this.getId().toString() + ") " + this.getTitulo();
    }
    
    public List<Trecho> getTrechosDeIda() {
        
        
        List<Trecho> trechos = new ArrayList();
        
        this.requisicaos.forEach((requisicao) -> {
            trechos.addAll(requisicao.getTrechosDeIda());
        });
        
        
        return trechos;
    }
    
    public List<Trecho> getTrechosDeVolta() {
        
        List<Trecho> trechos = new ArrayList();
        
        this.requisicaos.forEach((requisicao) -> {
            trechos.addAll(requisicao.getTrechosDeVolta());
        });
        
        return trechos;
    }
    
    public String getQuilometragemVeiculo(Long veiculo_id, ETipoQuilometragem tipo) {
        
        List<Quilometragem> quilometragem = (tipo == ETipoQuilometragem.KM_FINAL) ? this.getQuilometragemFinal() : this.getQuilometragemInicial();
        
        for (Quilometragem q : quilometragem) {
            
            if (q.getVeiculo().getId().equals(veiculo_id)) {
                return q.getQuilometro().toString();
            }
        }
        
        return "----";
    }

    public List<Quilometragem> getQuilometragemInicial() {
        return quilometragemInicial;
    }

    public void setQuilometragemInicial(List<Quilometragem> quilometragemInicial) {
        this.quilometragemInicial = quilometragemInicial;
    }

    public List<Quilometragem> getQuilometragemFinal() {
        return quilometragemFinal;
    }

    public void setQuilometragemFinal(List<Quilometragem> quilometragemFinal) {
        this.quilometragemFinal = quilometragemFinal;
    }
    
    public boolean hasQuilometragemInicialInserida() {
        return !this.quilometragemInicial.isEmpty() && 
                this.quilometragemInicial.size() >= this.veiculos.size() ;
    }
    
    public boolean hasQuilometragemFinalInserida() {
        return !this.quilometragemFinal.isEmpty() &&
                this.quilometragemFinal.size() >= this.veiculos.size();
    }
}
