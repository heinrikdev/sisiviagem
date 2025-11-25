package ufg.br.fabricasw.jatai.swsisviagem.repository;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RelatorioViagemRequisicaoProposto;

/**
 *
 * @author Ronaldo N Sousa
 * Criado em: 26/07/2019
 */
public interface RelatorioViagemRequisicaoPropostoRepository extends JpaRepository<RelatorioViagemRequisicaoProposto, Long> {

    RelatorioViagemRequisicaoProposto findByCodigoVerificacaoAndDataEmissaoAndIdentificador(Long codigoVerificacao, LocalDate dataEmissao, Integer identificador);
}
