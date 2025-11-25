package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoDiaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by thevilela on 02/02/17.
 */
@Repository
public interface SolicitacaoDiariaRepository extends JpaRepository<SolicitacaoDiaria, Long> {

    @Query("select s from SolicitacaoDiaria s where s.estadoSolicitacao = 'APROVADA'")
    public List<SolicitacaoDiaria> listarSolicitacoesAprovadas();

    @Query("select s from SolicitacaoDiaria s where s.estadoSolicitacao = 'RECUSADA'")
    public List<SolicitacaoDiaria> listarSolicitacoesRecusadas();

    @Query("select s from SolicitacaoDiaria s where s.estadoSolicitacao = 'EM_AVALIACAO'")
    public List<SolicitacaoDiaria> listarSolicitacoesEmAvaliacao();

}
