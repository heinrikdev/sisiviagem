package ufg.br.fabricasw.jatai.swsisviagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.ComprovanteViagem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprovanteViagemRepository extends JpaRepository<ComprovanteViagem, Long> {

    @Query("select c from ComprovanteViagem c where c.codigoVerificacao = :codigoVerificacao")
    public ComprovanteViagem findBycodigoVerificacao(@Param("codigoVerificacao") Long identificador);
}
