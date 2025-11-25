package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;

/**
 * Created by guilherme on 05/09/17.
 */
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    @Override
    public List<Pessoa> findAll();

    @Query("select p from Pessoa  p where p.siap like :siape ")
    public Pessoa findBySiape(@Param("siape") String siape);

    @Query("select p from Usuario u inner join u.pessoa as p where u.unidade.id = :idUnidade and u.tipoUsuario like 'Desacordo'")
    public List<Pessoa> findAutorizadorByUnidade(@Param("idUnidade") Long idUnidade);
    
    @Query("select p.email from Usuario u inner join u.pessoa as p where u.unidade.id = :idUnidade and u.tipoUsuario like 'Desacordo'")
    public List<String> listEmailAutorizadorByUnidade(@Param("idUnidade") Long idUnidade);

    @Query("select p from Usuario u inner join u.pessoa p where u.loginUnico like :loginUnico ")
    public Pessoa findByLogin(@Param("loginUnico") String loginUnico);
    
    @Query
    (
        "UPDATE Pessoa P set P.email = :new_email WHERE P.id = :pessoa_id"
    )
    @Modifying
    @Transactional
    public void updateEmail(@Param("pessoa_id") Long pessoa_id, @Param("new_email") String new_email);
    
    @Query
    (
        "SELECT P.infomacaoComoPassageiro FROM Pessoa P WHERE P.id = :id_pessoa"
    )
    Passageiro fetchInfoPassageir(@Param("id_pessoa") Long id_pessoa);
}
