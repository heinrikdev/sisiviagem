package ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums;

import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoCombustivel;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Service
public class ETipoCombustivelFormatter implements Formatter<ETipoCombustivel> {

    @Override
    public String print(ETipoCombustivel obj, Locale locale) {
        return obj != null ? obj.id().toString() : "";
    }

    @Override
    public ETipoCombustivel parse(String text, Locale locale) throws ParseException {
        
        Integer id = Integer.valueOf(text);
        
        for(ETipoCombustivel s : ETipoCombustivel.values()) {
            
            if (Objects.equals(s.id(), id)) {
                
                return s;
            }
        }
        return null;
    }
}