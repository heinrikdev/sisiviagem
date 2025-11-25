package ufg.br.fabricasw.jatai.swsisviagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.VeiculoProprio;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface VeiculoProprioRepository extends JpaRepository<VeiculoProprio, Long> {
    
    @Query
    (
        value = "DELETE FROM veiculo_proprio_trechos WHERE trechos_id = :trecho_id",
        nativeQuery = true
    )
    @Transactional
    @Modifying
    void removerTrecho(@Param("trecho_id") Long trechoId);
}
