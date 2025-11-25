package ufg.br.fabricasw.jatai.swsisviagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Justificativa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JustificativaRepository extends JpaRepository<Justificativa, Long> {

    @Query("select j from Justificativa j where j.codigoVerificacao = :codigoVerificacao")
    public Justificativa findBycodigoVerificacao(@Param("codigoVerificacao") Long identificador);
    
    @Query("select j from Justificativa j where j.requisicao.id = :id_requisicao")
    public Justificativa findByRequisicao(@Param("id_requisicao") Long id_requisicao);
}
