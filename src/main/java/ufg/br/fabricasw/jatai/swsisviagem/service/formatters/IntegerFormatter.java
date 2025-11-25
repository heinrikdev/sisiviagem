package ufg.br.fabricasw.jatai.swsisviagem.service.formatters;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ronaldo Nogueira de Sousa
 */
@Service
public class IntegerFormatter implements Formatter<Integer>{

    @Override
    public String print(Integer t, Locale locale) {

        NumberFormat nf = NumberFormat.getIntegerInstance(new Locale ("pt", "BR"));
        return nf.format(t);
    }

    @Override
    public Integer parse(String string, Locale locale) throws ParseException {
        
        NumberFormat nf = NumberFormat.getIntegerInstance(new Locale ("pt", "BR"));
        return nf.parse(string).intValue();
    }
    
}
