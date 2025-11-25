package ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums;

import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoViagem;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Service
public class EEstadoViagemFormatter implements Formatter<EEstadoViagem> {

    @Override
    public String print(EEstadoViagem obj, Locale locale) {
        return obj != null ? obj.toString() : "";
    }

    @Override
    public EEstadoViagem parse(String text, Locale locale) throws ParseException {
        
        Integer id = Integer.valueOf(text);
        
        for(EEstadoViagem s : EEstadoViagem.values()) {
            
            if (Objects.equals(s.id(), id)) {
                
                return s;
            }
        }
        return null;
    }
}