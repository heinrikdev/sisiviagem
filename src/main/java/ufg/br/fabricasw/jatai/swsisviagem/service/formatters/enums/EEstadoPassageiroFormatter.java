package ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums;

import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoPassageiro;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Service
public class EEstadoPassageiroFormatter implements Formatter<EEstadoPassageiro> {

    @Override
    public String print(EEstadoPassageiro object, Locale locale) {
        return (object != null) ? object.toString() : "";
    }

    @Override
    public EEstadoPassageiro parse(String text, Locale locale) throws ParseException {
        
        Integer id = Integer.parseInt(text);
        
        for (EEstadoPassageiro estado : EEstadoPassageiro.values()) {
            
            if (Objects.equals(estado.id(), id)) {
                return estado;
            }
        }
        
        return null;
    }
}
