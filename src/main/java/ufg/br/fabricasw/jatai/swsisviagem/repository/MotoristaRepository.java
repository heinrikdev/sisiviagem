package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Motorista;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface MotoristaRepository extends JpaRepository<Motorista, Long> {

    @Query("select m from Motorista m inner join m.pessoa as p where p.nome like %:nome%")
    public List<Motorista> findByName(@Param("nome") String nome);

    @Modifying
    @Transactional(readOnly = false)
    @Query("UPDATE Motorista m SET m.isAtivo = :new_status WHERE m.id = :id")
    public void alternarStatus(@Param("new_status") Boolean new_status, @Param("id") Long id);
    
    /**
     * Busca todos os motoristas, exceto os que foram escalados para uma viagem.
     * @param viagem_id id da viagem
     * @return 
     */
    @Query
    (
        value= "SELECT M.* FROM motorista M WHERE M.id NOT IN (SELECT motoristas_id FROM viagem_motoristas WHERE viagem_id = :viagem_id)",
        nativeQuery = true
    )
    public List<Motorista> findAllMotoristaExcetoNaViagem(@Param("viagem_id")Long viagem_id);
    
    @Query
    (
        value= "SELECT * FROM motorista as M WHERE M.is_ativo = :is_ativo",
        nativeQuery = true
    )
    public Page<Motorista> findAllMotoristaAtivosOrInativos(@Param("is_ativo") Boolean is_ativo, Pageable pageable);
    
}
