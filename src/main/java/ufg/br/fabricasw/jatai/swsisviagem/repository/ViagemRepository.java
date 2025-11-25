package ufg.br.fabricasw.jatai.swsisviagem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoViagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.repository.projection.ViagemInfoProjection;
import ufg.br.fabricasw.jatai.swsisviagem.repository.projection.ViagemSimpleProjection;

import java.time.LocalDate;
import java.util.List;

public interface ViagemRepository extends JpaRepository<Viagem, Long> {

    @Query("SELECT V FROM Viagem V WHERE V.data = :data")
    public List<Viagem> findByData(@Param("data") LocalDate data);

    @Query("SELECT V FROM Viagem V WHERE V.data BETWEEN :from AND :to AND V.estadoViagem = :estado_viagem ORDER BY V.data")
    public List<Viagem> findByRangeDate(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("estado_viagem") EEstadoViagem estadoViagem);

    @Query("select v from Viagem v inner join v.motoristas as m inner join m.pessoa as p where p.nome like :motorista ")
    public List<Viagem> findByMotorista(@Param("motorista") String motorista);

    @Query("select v from Viagem v inner join v.motoristas as m inner join m.pessoa as p where p.nome like :motorista and v.data like %:data ")
    public List<Viagem> findByMotoristaAndDate(@Param("motorista") String motorista, @Param("data") String data);

    @Query(value = "INSERT INTO viagem_requisicaos(viagem_id, requisicaos_id) VALUES(:viagem_id, :requisicao_id)", nativeQuery = true)
    public void adicionarRequisicao(@Param("viagem_id") Long viagem_id, @Param("requisicao_id") Long requisicao_id);

    @Query(
            "SELECT V FROM Viagem V WHERE V.estadoViagem = :status ORDER BY V.data"
    )
    public Page<Viagem> findAllByStatus(@Param("status") EEstadoViagem status, Pageable pageable);

    @Query(
            "SELECT V FROM Viagem V WHERE V.id = :viagem_id"
    )
    public Page<Viagem> findById(@Param("viagem_id") Long viagem_id, Pageable pageable);

    @Query(
            "SELECT V FROM Viagem V WHERE V.data BETWEEN :from AND :to "
    )
    public List<ViagemSimpleProjection> findAllViagensAgendadas(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query(
            "SELECT V FROM Viagem V INNER JOIN V.requisicaos R WHERE R.id = :requisicao_id ORDER BY V.data"
    )
    public List<ViagemInfoProjection> findAllViagemProgramadaPara(@Param("requisicao_id") Long requisicao_id);

    /**
     * Busca todas as viagnes que estão em aberta ou em andamento.
     *
     * @param motorista_id identificação do motorista
     * @param pageable
     * @return
     */
    @Query("SELECT V FROM Viagem V INNER JOIN V.motoristas AS M WHERE M.pessoa.id = :motorista_id AND (V.estadoViagem = 2 OR V.estadoViagem = 3)")
    public Page<Viagem> findByMotorista(@Param("motorista_id") Long motorista_id, Pageable pageable);

    @Query(
            "SELECT V FROM Viagem V "
            + "WHERE V.data BETWEEN :from AND :to AND "
            + "V.estadoViagem = :estado_viagem AND :req_id NOT IN (SELECT R.id FROM V.requisicaos R) "
            + "ORDER BY V.data"
    )
    public List<Viagem> findParaRequisicao(@Param("req_id") Long id, @Param("from") LocalDate from, @Param("to") LocalDate to, @Param("estado_viagem") EEstadoViagem estadoViagem);

    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM viagem_veiculos WHERE viagem_id = :viagem_id AND veiculos_id = :veiculos_id",
            nativeQuery = true
    )
    public void deleteVeiculoViagem(@Param("viagem_id") Long viagem_id, @Param("veiculos_id") Long veiculos_id);


    @Query("SELECT p.nome FROM Viagem v INNER JOIN v.motoristas as m INNER JOIN m.pessoa as p WHERE v.id = :viagem_id")
    public List<String> fetchMotoristaNomes(@Param("viagem_id") Long viagem_id);
}
