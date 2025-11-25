package ufg.br.fabricasw.jatai.swsisviagem.service;

import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.Role;
import ufg.br.fabricasw.jatai.swsisviagem.domain.ldap.UsuarioLdap;
import ufg.br.fabricasw.jatai.swsisviagem.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import ufg.br.fabricasw.jatai.swsisviagem.repository.ldap.UsuarioLdapRepository;
import ufg.br.fabricasw.jatai.swsisviagem.utils.SHA1HashGenerator;

/**
 * Created by guilherme on 06/09/17.
 */
@Service
public class UsuarioService implements UserDetailsService {

    public static final int USUARIO_SENHA_INVALIDA = 1;
    public static final int USUARIO_NAO_EXISTE = 2;
    public static final int USUARIO_AUTENTICADO = 3;
    public static final int USUARIO_DESATIVADO = 4;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioLdapRepository usuarioLdapRepository;

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String loginUnico) throws UsernameNotFoundException {

        Usuario u = repository.findByLoginUnico(loginUnico);

        if (u == null) {
            throw new UsernameNotFoundException(String.format("O login único %s não está cadastrado", loginUnico));
        }

        return (UserDetails) new User(loginUnico, u.getPassword(), u.getAuthorities());
    }

    public boolean verifyPasswordLdap(Usuario credentials) {

        // Gambiara safada para corrigir o problema dos logins que não são "logins
        // unicos" do ldap
        ArrayList<String> users = new ArrayList<>();
        users.add("proaddep".toLowerCase());
        users.add("uaeDEP".toLowerCase());
        users.add("admin".toLowerCase());
        users.add("transporte".toLowerCase());
        users.add("UAECIAGRA".toLowerCase());
        users.add("UAECIBIO".toLowerCase());
        users.add("UAECHL".toLowerCase());
        users.add("UAECISAU".toLowerCase());
        users.add("UAECSA".toLowerCase());
        users.add("UAEEGEO".toLowerCase());
        users.add("UAEEDU".toLowerCase());
        users.add("proadufj".toLowerCase());
        users.add("posprofmat".toLowerCase());
        users.add("posresidmmv".toLowerCase());
        users.add("posagronomia".toLowerCase());
        users.add("posbioanimal".toLowerCase());
        users.add("ppgcas".toLowerCase());
        users.add("poseducacao".toLowerCase());
        users.add("posgeografia".toLowerCase());
        users.add("copgDEP".toLowerCase());
        users.add("admin_marcos".toLowerCase());
        users.add("transpadmin".toLowerCase());
        users.add("UAECIEXA".toLowerCase());
        users.add("wendell.silva".toLowerCase());
        users.add("ppgq".toLowerCase());
        users.add("ppgd".toLowerCase());
        users.add("ppgbiodiv".toLowerCase());

        //Cadastro de login para servidores que tem portaria para dirigir veículo oficial mas quem também são requisitantes de viagens.
        users.add("taironehf".toLowerCase()); // Tairone Honório de Freitas
        users.add("marinho_motorista".toLowerCase()); // Francismário Ferreira dos Santos
        users.add("christiano_motorista".toLowerCase()); // Christiano Peres Coelho
        

        if (true) {
            Usuario u = repository.findByLoginUnico(credentials.getLoginUnico());
            return verifyPassword(credentials, u);
        }

        Optional<UsuarioLdap> byLoginUnicoAndPassword = usuarioLdapRepository.findByLoginUnicoAndPassword(
                credentials.getLoginUnico(), SHA1HashGenerator.ldapHash(credentials.getPassword()));

        return byLoginUnicoAndPassword.isPresent();

    }

    /**
     * Loga o usuário no sistema.
     *
     * @param credentials - Objeto Usuario com login e senha
     * @return UsuarioService.USUARIO_NAO_EXISTE,
     *         UsuarioService.USUARIO_AUTENTICADO,
     *         UsuarioService.USUARIO_SENHA_INVALIDA ou
     *         UsuarioService.USUARIO_DESATIVADO
     */
    public int logarUsuario(Usuario credentials) {

        Usuario usr = this.findByLoginUnico(credentials.getLoginUnico().trim());

        if (usr == null) {

            return UsuarioService.USUARIO_NAO_EXISTE;
        }

        if (!usr.isEnabled()) {

            return UsuarioService.USUARIO_DESATIVADO;
        }

        if (this.verifyPasswordLdap(credentials)) {

            this.setAuthentication(usr);
            return UsuarioService.USUARIO_AUTENTICADO;
        }

        return UsuarioService.USUARIO_SENHA_INVALIDA;
    }

    /**
     * Muda a visão do sistema de admin para o usuário selecionado.
     * 
     * @param usuario -
     */
    public void logarComo(Usuario usuario) {

        this.setAuthentication(usuario);
    }

    public Usuario save(Usuario entity) {
        return repository.save(entity);
    }

    public Usuario findByLoginUnico(String loginUnico) {
        return repository.findByLoginUnico(loginUnico);
    }

    public Usuario findUserBySiap(String siap) {
        return repository.findBySiap(siap);
    }

    public Usuario findUserByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    public Usuario findUserLogado() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        try {

            Usuario user = (Usuario) auth.getPrincipal();
            return user != null ? user : new Usuario();

        } catch (Exception e) {
            return null;
        }
    }

    public List<Usuario> findByNome(String nome) {
        return repository.findByName(nome);
    }

    public Usuario find(Long id) {
        return repository.getOne(id);
    }

    public void delete(Usuario user) {
        repository.delete(user);
    }

    public void update(Usuario user) {
        repository.saveAndFlush(user);
    }

    public Usuario findByPessoa(Pessoa pessoa) {
        return repository.findByPessoa(pessoa);
    }

    public List<Usuario> findAllDesacordo() {
        return repository.findAllDesacordo();
    }

    public List<String> findEmailFromDesacordo(Role role, Long unidade_id) {
        return this.repository.findEmailFromDesacordo(role.toString(), unidade_id);
    }

    public List<String> findEmailFromDesacordoTransporte() {
        return this.repository.findEmailFromDesacordoTransporte(Role.TRANSPORTE.toString());
    }

    /**
     * Faz o logout do usuário.
     * 
     * @param request  -
     * @param response -
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) {

        SecurityContextLogoutHandler app = new SecurityContextLogoutHandler();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        app.setClearAuthentication(true);
        app.setInvalidateHttpSession(true);
        app.logout(request, response, auth);
    }

    /**
     * Busca todos os usuários que fazem requisições ao sistema.
     * 
     * @param pageable
     * @return
     */
    public Page<Usuario> findAllUsuarios(Pageable pageable) {
        return this.repository.findaAllUsuarios(pageable);
    }

    /**
     * Busca todos os usuários que fazem requisições ao sistema por nome.
     * 
     * @param nome
     * @param pageable
     * @return
     */
    public Page<Usuario> findAllUsuarios(String nome, Pageable pageable) {

        if (nome == null) {

            return this.findAllUsuarios(pageable);
        }

        return this.repository.findaAllUsuarios(nome, pageable);
    }

    /**
     * Busca todos os usuários que acitam ou rejeitam as requisições por nome
     * 
     * @param nome
     * @param pageable
     * @return
     */
    public Page<Usuario> findAllProponente(String nome, Pageable pageable) {

        if (nome == null) {

            return this.findAllProponente(pageable);
        }

        return repository.findAllProponentes(nome, pageable);
    }

    /**
     * Busca todos os usuários que acitam ou rejeitam as requisições
     * 
     * @param pageable
     * @return
     */
    public Page<Usuario> findAllProponente(Pageable pageable) {

        return repository.findAllProponentes(pageable);
    }

    public void atualizarContextoUsuario(Usuario usr) {
        this.setAuthentication(usr);
    }

    private void setAuthentication(Usuario usr) {

        Authentication auth = new UsernamePasswordAuthenticationToken(usr, usr.getPassword(), usr.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public boolean verifyPassword(Usuario credentials, Usuario usr) {
        return this.passwordEncoder.matches(credentials.getPassword(), usr.getPassword());
    }

    public boolean verifyPassword(String rawPassword, String encryptedPassword) {
        return this.passwordEncoder.matches(rawPassword, encryptedPassword);
    }

    /**
     * Encripta a senha.
     * 
     * @param rawPassword senha vinda do formulário.
     * @return a senha encrypted
     */
    public String encryptPassword(String rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }

    /**
     * Verifica se o login unico está em uso.
     * 
     * @param login_unico
     * @return
     */
    public Boolean loginUnicoEstaEmUso(String login_unico) {
        return this.repository.loginUnicoEmUso(login_unico);
    }

    /**
     * Ativa ou desativa um usuario.
     * 
     * @param usuario_id id do usuario.
     * @param status     o novo status do usuario.
     */
    public void desativarAtivar(Long usuario_id, Boolean status) {
        this.repository.desativarAtivar(usuario_id, status);
    }

    public void updatePassword(Long usuario_id, String password) {
        this.repository.updatePassword(usuario_id, password);
    }
}
