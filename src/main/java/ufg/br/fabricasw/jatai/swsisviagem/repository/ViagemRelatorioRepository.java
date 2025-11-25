package ufg.br.fabricasw.jatai.swsisviagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.ViagemRelatorio;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ViagemRelatorioRepository extends JpaRepository<ViagemRelatorio, Long> {

    @Query("select f from ViagemRelatorio f where f.codigoVerificacao = :codigoVerificacao")
    public ViagemRelatorio findBycodigoVerificacao(@Param("codigoVerificacao") Long identificador);

}
