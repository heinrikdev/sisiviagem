package ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums;

import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoServico;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Service
public class ETipoServicoFormatter implements Formatter<ETipoServico> {

    @Override
    public String print(ETipoServico obj, Locale locale) {
        return obj != null ? obj.id().toString() : "";
    }

    @Override
    public ETipoServico parse(String text, Locale locale) throws ParseException {
        
        Integer id = Integer.valueOf(text);
        
        for(ETipoServico s : ETipoServico.values()) {
            
            if (Objects.equals(s.id(), id)) {
                
                return s;
            }
        }
        return null;
    }
}