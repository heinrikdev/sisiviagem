package ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums;

import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EInfoTrajetoPassageiro;

/**
 *
 * @author ronogue
 */
@Service
public class EInfoTrajetoPassageiroFormatter implements Formatter<EInfoTrajetoPassageiro> {

    @Override
    public String print(EInfoTrajetoPassageiro obj, Locale locale) {
        return obj != null ? obj.id().toString() : "";
    }

    @Override
    public EInfoTrajetoPassageiro parse(String text, Locale locale) throws ParseException {
        Integer id = Integer.valueOf(text);
        return EInfoTrajetoPassageiro.values()[id];
    }
    
}
