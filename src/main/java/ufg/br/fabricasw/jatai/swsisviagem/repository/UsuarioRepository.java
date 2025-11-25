package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by guilherme on 05/09/17.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("select usuario from Usuario usuario where usuario.loginUnico like :loginUnico")
    public Usuario findByLoginUnico(@Param("loginUnico") String loginUnico);

    @Query("select usuario from Usuario usuario inner join usuario.pessoa as p where p.siap like :siap")
    public Usuario findBySiap(@Param("siap") String siap);

    @Query("select usuario from Usuario usuario inner join usuario.pessoa as p where p.cpf like :cpf")
    public Usuario findByCpf(@Param("cpf") String cpf);

    @Query("select u from Usuario u inner join u.pessoa as p where p.nome like %:nome%")
    public List<Usuario> findByName(@Param("nome") String nome);

    @Query("select u from Usuario u where u.pessoa = :pessoa")
    public Usuario findByPessoa(@Param("pessoa") Pessoa pessoa);
    
    /**
     * Verifica se um login unico está em uso.
     * 
     * @param login_unico login unico.
     * @return 
     */
    @Query
    (
        "SELECT COUNT(U.loginUnico) = 1 FROM Usuario U WHERE U.loginUnico = :login_unico "
    )
    public Boolean loginUnicoEmUso(@Param("login_unico") String login_unico);
    
    /**
     * Busca todos os usuários que fazem requisições de viagem no sisteam por nome.
     * @param nome
     * @param pageable
     * @return - Lista de usuários.
     */
    @Query("select u from Usuario u inner join u.pessoa as p where p.nome like %:nome%")
    public Page<Usuario> findaAllUsuarios(@Param("nome") String nome, Pageable pageable);
    
    /**
     * Busca todos os usuários que fazem requisições de viagem no sisteam.
     * @param pageable
     * @return - Lista de usuários.
     */
    @Query("select u from Usuario  u where u.tipoUsuario like 'Proponente'")
    public Page<Usuario> findaAllUsuarios(Pageable pageable);

    /**
     * Seleciona todos os proponentes do sistema pelo nome.
     * @param nome
     * @param pageable
     * @return - Página contendo os proponentes.
     */
    @Query("select u from Usuario  u where u.tipoUsuario like 'Desacordo' and u.loginUnico like %:nome%")
    public Page<Usuario> findAllProponentes(@Param("nome") String nome, Pageable pageable);
    
    /**
     * Seleciona todos os proponentes do sistema.
     * @param pageable
     * @return - Página contendo os proponentes.
     */
    @Query("select u from Usuario  u where u.tipoUsuario like 'Desacordo'")
    public Page<Usuario> findAllProponentes(Pageable pageable);
    
    /**
     * Seleciona todos os proponentes do sistema.
     * @return - Lista contendo os proponentes.
     */
    @Query("select u from Usuario  u where u.tipoUsuario like 'Desacordo'")
    public List<Usuario> findAllDesacordo();

    /**
     * Desacordo -> Não foi o ronogue que criou este termon, não sei o porquê disso, mas no sistema são
     * os usuários que avaliam as solicitações mas não tenho certeza.
     * @param unidade_id id da unidade para qual a requisição foi direcionada
     * @return lista de emails
     */
    @Query("SELECT U.pessoa.email FROM Usuario U INNER JOIN U.roles R WHERE U.tipoUsuario = 'Desacordo' AND :role in R AND U.unidade.id = :unidade_id")
    public List<String> findEmailFromDesacordo(@Param("role") String role, @Param("unidade_id") Long unidade_id);
    
    @Query("SELECT U.pessoa.email FROM Usuario U INNER JOIN U.roles R WHERE U.tipoUsuario = 'Desacordo' AND :role in R")
    public List<String> findEmailFromDesacordoTransporte(@Param("role") String role);
    
    @Query
    (
        "UPDATE Usuario U set U.enabled = :status WHERE U.id = :usuario_id"
    )
    @Modifying
    @Transactional
    public void desativarAtivar(@Param("usuario_id") Long usuario_id, @Param("status") Boolean status);
    
    @Query
    (
        "UPDATE Usuario U set U.password = :new_password WHERE U.id = :usuario_id"
    )
    @Modifying
    @Transactional
    public void updatePassword(@Param("usuario_id") Long usuario_id, @Param("new_password") String new_password);

}
