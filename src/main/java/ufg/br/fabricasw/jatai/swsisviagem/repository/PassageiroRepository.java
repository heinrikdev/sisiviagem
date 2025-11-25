package ufg.br.fabricasw.jatai.swsisviagem.repository;

import java.util.List;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PassageiroRepository extends JpaRepository<Passageiro, Long> {

    @Query
    (
        "SELECT P FROM Passageiro P WHERE P.id = :id"
    )
    Passageiro getOneById(@Param("id") Long id);
    
    List<Passageiro> findByNomeContaining(String nome);
}
