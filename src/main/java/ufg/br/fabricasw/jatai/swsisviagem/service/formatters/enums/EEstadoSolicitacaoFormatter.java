package ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums;

import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Service
public class EEstadoSolicitacaoFormatter implements Formatter<EEstadoSolicitacao> {

    @Override
    public String print(EEstadoSolicitacao obj, Locale locale) {
        return obj != null ? obj.id().toString() : "";
    }

    @Override
    public EEstadoSolicitacao parse(String text, Locale locale) throws ParseException {
        Integer id = Integer.valueOf(text);
        return EEstadoSolicitacao.values()[id];
    }
    
}