package ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums;

import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EUnidadeDepartamento;

/**
 *
 * @author ronogue
 */
@Service
public class EUnidadeDepartamentoFormatter implements Formatter<EUnidadeDepartamento>{

    @Override
    public String print(EUnidadeDepartamento obj, Locale locale) {
        return obj != null ? obj.id().toString() : "";
    }

    @Override
    public EUnidadeDepartamento parse(String text, Locale locale) throws ParseException {
        
        Integer id = Integer.valueOf(text);
        return EUnidadeDepartamento.values()[id];
    }
}
