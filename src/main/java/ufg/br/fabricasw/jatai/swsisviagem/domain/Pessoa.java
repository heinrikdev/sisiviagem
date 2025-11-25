package ufg.br.fabricasw.jatai.swsisviagem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.ParamFieldName;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.SupportsCustomizedBinding;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;

/**
 * Created by guilherme on 05/09/17.
 */
@Entity
@SupportsCustomizedBinding
public class Pessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ParamFieldName("pessoa_id")
    private Long id;

    private String nome;

    private String email;

    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "siap", unique = true)
    private String siap;
    
    @JsonIgnore
    @OneToOne(cascade =  CascadeType.ALL, mappedBy = "pessoa")
    private Usuario usuario;
    
    @JsonIgnore
    @OneToOne(cascade =  CascadeType.ALL)
    @JoinColumn(name = "infomacao_como_passageiro_id", nullable = true)
    private Passageiro infomacaoComoPassageiro;

    private LocalDate dataNascimento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return (nome != null) ? nome.toUpperCase() : "";
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSiap() {
        return (siap != null) ? siap : "----";
    }

    public void setSiap(String siap) {
        this.siap = siap;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pessoa pessoa = (Pessoa) o;

        if (getId() != null ? !id.equals(pessoa.id) : pessoa.getId() != null) {
            return false;
        }
        if (getNome() != null ? !nome.equals(pessoa.nome) : pessoa.getNome() != null) {
            return false;
        }
        if (getEmail() != null ? !email.equals(pessoa.email) : pessoa.getEmail() != null) {
            return false;
        }
        if (getSiap() != null ? !siap.equals(pessoa.siap) : pessoa.getSiap() != null) {
            return false;
        }
        return getDataNascimento() != null ? getDataNascimento().equals(pessoa.getDataNascimento()) : pessoa.getDataNascimento() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getNome() != null ? getNome().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getSiap() != null ? getSiap().hashCode() : 0);
        result = 31 * result + (getDataNascimento() != null ? getDataNascimento().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return 
        "\n\t\tPessoa {" + 
                "\n\t\t\tid=" + getId() + 
                ",\n\t\t\tnome=" + getNome() + 
                ",\n\t\t\temail=" + getEmail() + 
                ",\n\t\t\tcpf=" + getCpf() + 
                ",\n\t\t\tsiap=" + getSiap() + 
                ",\n\t\t\tdataNascimento=" + getDataNascimento() + 
        "\n\t}";
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return (cpf != null) ? cpf : "----";
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the infomacaoComoPassageiro
     */
    public Passageiro getInfomacaoComoPassageiro() {
        return infomacaoComoPassageiro;
    }

    /**
     * @param infomacaoComoPassageiro the infomacaoComoPassageiro to set
     */
    public void setInfomacaoComoPassageiro(Passageiro infomacaoComoPassageiro) {
        this.infomacaoComoPassageiro = infomacaoComoPassageiro;
    }
    
    public boolean isCadastroCompleto() {
        
        if (!this.getUsuario().isProposto()) return true;
        
        return this.getInfomacaoComoPassageiro() != null;
    }
    
    public String getContato() {
        
        if (this.infomacaoComoPassageiro != null) {
            
            return this.infomacaoComoPassageiro.getTelefone();
        }
        
        return "Não informado.";
    }
}
