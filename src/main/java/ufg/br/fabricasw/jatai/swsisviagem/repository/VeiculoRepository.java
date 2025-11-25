package ufg.br.fabricasw.jatai.swsisviagem.repository;

import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Veiculo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by guilherme on 24/11/17.
 */
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query("select v from Veiculo v where v.carro like :nome or v.placa like :nome or v.marca like :nome ")
    public List<Veiculo> findByInfo(@Param("nome") String nome);
    
    @Modifying
    @Transactional
    @Query("UPDATE Veiculo v SET v.isAtivo = :new_status WHERE v.id = :id")
    public void alternarStatus(@Param("id")Long id, @Param("new_status")Boolean new_status);
    
    /**
     * Busca todos os veículos, exceto os que foram escalados para uma determinada viagem.
     * @param viagem_id id da viagem
     * @return 
     */
    @Query
    (
        value = "SELECT V.* FROM veiculo V WHERE V.is_ativo = 1 AND V.id NOT IN (SELECT veiculos_id FROM viagem_veiculos WHERE viagem_id = :viagem_id)",
        nativeQuery = true
    )
    public List<Veiculo> findAllVeiculoExcetoNaViagem(@Param("viagem_id") Long viagem_id);    
    
    @Query("SELECT v FROM Veiculo v WHERE v.isAtivo = :is_ativo")
    Page<Veiculo> findAllVeiculosAtivosOrInativos(@Param("is_ativo") Boolean is_ativo, Pageable pageable);
}
