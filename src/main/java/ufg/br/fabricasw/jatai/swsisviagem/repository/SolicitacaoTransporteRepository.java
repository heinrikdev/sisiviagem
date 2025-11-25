package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoTransporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by thevilela on 02/02/17.
 */
@Repository
public interface SolicitacaoTransporteRepository extends JpaRepository<SolicitacaoTransporte, Long> {

    @Query("select s from SolicitacaoTransporte s where s.estadoSolicitacao = 'APROVADA'")
    public List<SolicitacaoTransporte> listarSolicitacoesAprovadas();

    @Query("select s from SolicitacaoTransporte s where s.estadoSolicitacao = 'RECUSADA'")
    public List<SolicitacaoTransporte> listarSolicitacoesRecusadas();

    @Query("select s from SolicitacaoTransporte s where s.estadoSolicitacao = 'EM_AVALIACAO'")
    public List<SolicitacaoTransporte> listarSolicitacoesEmAvaliacao();

}
