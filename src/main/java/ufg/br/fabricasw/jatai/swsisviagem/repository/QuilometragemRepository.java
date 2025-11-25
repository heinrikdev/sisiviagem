package ufg.br.fabricasw.jatai.swsisviagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Quilometragem;
import org.springframework.stereotype.Repository;

@Repository
public interface QuilometragemRepository extends JpaRepository<Quilometragem, Long> {

}
