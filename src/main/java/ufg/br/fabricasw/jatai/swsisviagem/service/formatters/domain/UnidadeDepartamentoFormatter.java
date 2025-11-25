package ufg.br.fabricasw.jatai.swsisviagem.service.formatters.domain;

import java.text.ParseException;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.UnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.service.UnidadeService;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Service
public class UnidadeDepartamentoFormatter implements Formatter<UnidadeDepartamento>{

    @Autowired
    private UnidadeService unidadeService;
    
    @Override
    public String print(UnidadeDepartamento object, Locale locale) {
        return object != null ? object.getId().toString() : "";
    }

    @Override
    public UnidadeDepartamento parse(String text, Locale locale) throws ParseException {
        
        Long id = Long.valueOf(text);
        return unidadeService.findOne(id);
    }
}