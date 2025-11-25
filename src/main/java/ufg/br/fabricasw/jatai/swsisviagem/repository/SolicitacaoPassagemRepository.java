package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoPassagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by thevilela on 02/02/17.
 */
@Repository
public interface SolicitacaoPassagemRepository extends JpaRepository<SolicitacaoPassagem, Long> {

    @Query("select s from SolicitacaoPassagem s where s.estadoSolicitacao = 'APROVADA'")
    public List<SolicitacaoPassagem> listarSolicitacoesAprovadas();

    @Query("select s from SolicitacaoPassagem s where s.estadoSolicitacao = 'RECUSADA'")
    public List<SolicitacaoPassagem> listarSolicitacoesRecusadas();

    @Query("select s from SolicitacaoPassagem s where s.estadoSolicitacao = 'EM_AVALIACAO'")
    public List<SolicitacaoPassagem> listarSolicitacoesEmAvaliacao();

}
