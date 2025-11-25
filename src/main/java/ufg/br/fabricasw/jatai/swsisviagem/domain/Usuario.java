package ufg.br.fabricasw.jatai.swsisviagem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.Role;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoUsuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EUnidadeDepartamento;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.ParamFieldName;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.SupportsCustomizedBinding;

/**
 * Created by guilherme on 05/09/17.
 */
@Entity
@SupportsCustomizedBinding
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ParamFieldName("usuario_id")
    private Long id;

    private String loginUnico;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    private String password;

    private Boolean enabled = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    private Role currentRole;

    @Enumerated(EnumType.STRING)
    private EUnidadeDepartamento unidadeDepartamento;

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "unidade_id")
    private UnidadeDepartamento unidade;

    @Enumerated(EnumType.STRING)
    private ETipoUsuario tipoUsuario;

    @Transient
    private String perfil;

    public EUnidadeDepartamento getUnidadeDepartamento() {
        return unidadeDepartamento;
    }

    public void setUnidadeDepartamento(EUnidadeDepartamento unidadeDepartamento) {
        this.unidadeDepartamento = unidadeDepartamento;
    }

    public UnidadeDepartamento getUnidade() {
        return unidade;
    }

    public void setUnidade(UnidadeDepartamento unidade) {
        this.unidade = unidade;
    }

    public ETipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(ETipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
    }

    public Role getCurrentRole() {
        if (currentRole == null) {
            if(!roles.isEmpty()){
                currentRole = roles.get(0);
            } else {
                currentRole = Role.USER; // Default role if none is set
            }

        }
        return currentRole;
    }

    public String getLoginUnico() {
        return loginUnico;
    }

    public void setLoginUnico(String loginUnico) {
        this.loginUnico = loginUnico;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void md5sumPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(password.getBytes(), 0, password.length());
        this.password = new BigInteger(1, m.digest()).toString(16);
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginUnico;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }

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
     * Proponente é que aprova as requisições, por exempo: transporte, diária e
     * passagem.
     *
     * @return true caso afirmativo, false do contrário.
     */
    public Boolean isProponente() {

        if (this.getTipoUsuario() == null) {
            return false;
        }

        return this.getTipoUsuario().equals(ETipoUsuario.Desacordo);
    }

    /**
     * Proposto é que faz a requisição, ou seja, o usuário com perfil USER.
     *
     * Houve confusão no início do projeto da versão anterior a esta e por isso
     * está como Proponente. Falta arrumar no banco de dados.
     *
     * @return true caso afirmativo, false do contrário.
     */
    public Boolean isProposto() {
        return this.getTipoUsuario().equals(ETipoUsuario.Proponente);
    }

    @Override
    public String toString() {
        return
        "\n\t\tUsuario {"
            + "\n\t\t\tid=" + id + ','
            + "\n\t\t\tloginUnico=" + loginUnico + ','
            + "\n\t\t\tpassword=" + password + ','
            + "\n\t\t\tenabled=" + enabled + ','
            + "\n\t\t\troles=" + roles + ','
            + "\n\t\t\tunidadeDepartamento=" + unidadeDepartamento + ','
            + "\n\t\t\tunidade=" + unidade + ','
            + "\n\t\t\ttipoUsuario=" + tipoUsuario + ','
                + "\n\t\t\tPessoa=" + this.pessoa + ','
        + "\n}";
    }

    public String getPerfil() {

        if(this.currentRole == null) {
                if(this.getRoles().contains(Role.USER)) {
                    this.setPerfil("USUÁRIO");
                    currentRole = Role.USER;
                }
                if (this.getRoles().contains(Role.DIARIA) && this.getRoles().contains(Role.PASSAGEM)) {
                    this.setPerfil("PASSAGEM");
                    currentRole = Role.PASSAGEM;
                }
                else if (this.getRoles().contains(Role.TRANSPORTE)) {
                    this.setPerfil("TRANSPORTE");
                    currentRole = Role.TRANSPORTE;
                }
                else if (this.getRoles().contains(Role.ADMIN)) {
                    this.setPerfil("ADMINISTRADOR");
                    currentRole = Role.ADMIN;
                }
                else if (this.getRoles().contains(Role.AUDITOR)) {
                    this.setPerfil("AUDITOR");
                    currentRole = Role.AUDITOR;
                }
        }

        Role role = getCurrentRole();

        switch (role) {
            case USER:
                perfil = "USUÁRIO";
                break;
            case DIARIA:
            case PASSAGEM:
                perfil = "PASSAGEM";
                break;
            case TRANSPORTE:
                perfil = "TRANSPORTE";
                break;
            case ADMIN:
                perfil = "ADMINISTRADOR";
                break;
            case AUDITOR:
                perfil = "AUDITOR";
                break;
            default:
                perfil = "DESCONHECIDO";
                break;
       }

        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }
}
