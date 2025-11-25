package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    @Query("select m from Solicitacao s inner join s.mensagens as m  where s.id = :idSolicitacao  order by m.id desc")
    public List<Mensagem> findBySolicitacao(@Param("idSolicitacao") Long idSolicitacao);

}
