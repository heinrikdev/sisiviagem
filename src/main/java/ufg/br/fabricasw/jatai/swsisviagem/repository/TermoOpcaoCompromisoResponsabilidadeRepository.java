package ufg.br.fabricasw.jatai.swsisviagem.repository;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.TermoOpcaoCompromissoResponsabilidade;

/**
 *
 * @author Ronaldo N Sousa
 * Criado em: 22/07/2019
 */
public interface TermoOpcaoCompromisoResponsabilidadeRepository extends JpaRepository<TermoOpcaoCompromissoResponsabilidade, Long> {

    TermoOpcaoCompromissoResponsabilidade findByCodigoVerificacaoAndDataEmissaoAndIdentificador(Long codigoVerificacao, LocalDate dataEmissao, Integer identificador);
}
