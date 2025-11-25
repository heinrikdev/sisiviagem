package ufg.br.fabricasw.jatai.swsisviagem.domain.viagem;

import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoTarefa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Tarefa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private EEstadoTarefa estadoTarefa = EEstadoTarefa.Aberta;

    private String titulo;

    private String observacao;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    private String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EEstadoTarefa getEstadoTarefa() {
        return estadoTarefa;
    }

    public void setEstadoTarefa(EEstadoTarefa estadoTarefa) {
        this.estadoTarefa = estadoTarefa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tarefa tarefa = (Tarefa) o;
        
        return tarefa.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (estadoTarefa != null ? estadoTarefa.hashCode() : 0);
        result = 31 * result + (titulo != null ? titulo.hashCode() : 0);
        result = 31 * result + (observacao != null ? observacao.hashCode() : 0);
        result = 31 * result + (descricao != null ? descricao.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tarefa{"
                + "id=" + id
                + ", estadoTarefa=" + estadoTarefa
                + ", titulo='" + titulo + '\''
                + ", observacao='" + observacao + '\''
                + ", descricao='" + descricao + '\''
                + '}';
    }
    
    public boolean isAberta() {
        return this.estadoTarefa == EEstadoTarefa.Aberta;
    }
    
    public boolean isConcluida() {
        return this.estadoTarefa == EEstadoTarefa.Concluida;
    }
    
    public boolean isNaoRealizada() {
        return this.estadoTarefa == EEstadoTarefa.NaoRealizada;
    }
    
    public String getLabelColorStatus() {
        return this.isAberta() ? "label-warning" : this.isConcluida() ? "label-success" : "label-danger";
    }

}
