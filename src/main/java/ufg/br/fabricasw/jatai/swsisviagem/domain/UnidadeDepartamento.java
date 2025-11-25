package ufg.br.fabricasw.jatai.swsisviagem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EUnidadeDepartamento;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by guilherme on 05/10/17.
 */
@Entity
public class UnidadeDepartamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String nome;

    @Enumerated(EnumType.STRING)
    private EUnidadeDepartamento unidadeDepartamento;

    @OneToOne(mappedBy = "unidade", fetch = FetchType.LAZY)
    @JsonIgnore
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EUnidadeDepartamento getUnidadeDepartamento() {
        return unidadeDepartamento;
    }

    public void setUnidadeDepartamento(EUnidadeDepartamento unidadeDepartamento) {
        this.unidadeDepartamento = unidadeDepartamento;
    }

    @Override
    public String toString() {
        return "\n\tUnidadeDepartamento {" + 
            "\n\t\tid=" + id + 
            ", \n\t\tnome=" + nome + 
            ", \n\t\tunidadeDepartamento=" + unidadeDepartamento + 
        "\n\t}";
    }
}
