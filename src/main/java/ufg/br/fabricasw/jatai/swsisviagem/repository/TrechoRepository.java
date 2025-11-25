package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Trecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by thevilela on 02/02/17.
 */
@Repository
public interface TrechoRepository extends JpaRepository<Trecho, Long> {

    @Query
    (
        value = "DELETE FROM solicitacao_trechos WHERE trechos_id = :trecho_id",
        nativeQuery = true
    )
    @Transactional
    @Modifying
    void removerRelacionamentoComSolicitacao(@Param("trecho_id") Long trecho_id);
    
    
     @Query
    (
        value = "DELETE FROM veiculo_proprio_trechos WHERE trechos_id = :trecho_id",
        nativeQuery = true
    )
    @Transactional
    @Modifying
    void removerRelacionamentoComVeiculoProprio(@Param("trecho_id") Long trecho_id);
    
    @Query
    (
        value = "DELETE FROM trecho WHERE id = :trecho_id",
        nativeQuery = true
    )
    @Transactional
    @Modifying
    void removerTrecho(@Param("trecho_id") Long trecho_id);
}
