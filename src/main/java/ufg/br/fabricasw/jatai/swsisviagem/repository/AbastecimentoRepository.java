package ufg.br.fabricasw.jatai.swsisviagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Abastecimento;
import org.springframework.stereotype.Repository;

@Repository
public interface AbastecimentoRepository extends JpaRepository<Abastecimento, Long> {
}
