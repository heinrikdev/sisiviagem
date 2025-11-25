package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by thevilela on 02/02/17.
 */
@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    @Query("select s from Solicitacao s where s.estadoSolicitacao = 'APROVADA'")
    public List<Solicitacao> listarSolicitacoesAprovadas();

    @Query("select s from Solicitacao s where s.estadoSolicitacao = 'RECUSADA'")
    public List<Solicitacao> listarSolicitacoesRecusadas();

    @Query("select s from Solicitacao s where s.estadoSolicitacao = 'EM_AVALIACAO'")
    public List<Solicitacao> listarSolicitacoesEmAvaliacao();

}
