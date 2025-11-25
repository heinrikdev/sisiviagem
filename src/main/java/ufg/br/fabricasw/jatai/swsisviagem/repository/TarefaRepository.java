package ufg.br.fabricasw.jatai.swsisviagem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Tarefa;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Query
    (
        "UPDATE Tarefa T SET T.estadoTarefa = 0 WHERE T.id = :id"
    )
    @Modifying
    @Transactional
    void tarefaConcluida(@Param("id") Long tarefa_id);
    
    @Query
    (
        "UPDATE Tarefa T SET T.estadoTarefa = 1 WHERE T.id = :id"
    )
    @Modifying
    @Transactional
    void tarefaNaoRealizada(@Param("id") Long tarefa_id);
    
    @Query
    (
        value = "SELECT T.* FROM tarefa T INNER JOIN viagem_tarefas VT ON VT.tarefas_id = T.id WHERE VT.viagem_id = :viagem_id",
        nativeQuery = true
    )
    List<Tarefa> findAll(@Param("viagem_id") Long viagem_id);
    
    @Query
    (
        value = "SELECT viagem_id FROM viagem_tarefas WHERE tarefas_id = :tarefa_id",
        nativeQuery = true
    )
    Long findViagemByTarefa(@Param("tarefa_id") Long tarefa_id);
}
