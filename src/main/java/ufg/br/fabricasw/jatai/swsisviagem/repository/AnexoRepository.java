package ufg.br.fabricasw.jatai.swsisviagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AnexoRepository extends JpaRepository<Anexo, Long> {

    /**
     * Deleta um anexo do banco de dados.
     * @param anexo_id id do anexo
     */
    @Transactional
    @Modifying
    @Query(
        value = "DELETE rs, a FROM requisicao_anexos rs INNER JOIN anexo a ON a.id = rs.anexos_id WHERE a.id = :anexo_id",
        nativeQuery = true
    )
    void deleteAnexo(@Param("anexo_id") Long anexo_id);
    
    /**
     * Deleta um anexo do banco de dados.
     * @param anexo_id id do anexo
     */
    @Transactional
    @Modifying
    @Query(
        value = "DELETE rs, a FROM viagem_anexos rs INNER JOIN anexo a ON a.id = rs.anexos_id WHERE a.id = :anexo_id",
        nativeQuery = true
    )
    void deleteAnexoViagem(@Param("anexo_id") Long anexo_id);
    
    @Query
    (
        "SELECT A FROM Anexo A WHERE A.id = :anexo_id "
    )
    Anexo getById(@Param("anexo_id") Long anexo_id);
}
