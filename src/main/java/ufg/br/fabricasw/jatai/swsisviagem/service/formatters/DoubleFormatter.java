package ufg.br.fabricasw.jatai.swsisviagem.service.formatters;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ronaldo Nogueira de Sousa
 */
@Service
public class DoubleFormatter implements Formatter<Double>{

    @Override
    public String print(Double t, Locale locale) {
        
        String valorFormatado = new DecimalFormat("#,##0.00").format(t);
        return valorFormatado;
    }

    @Override
    public Double parse(String string, Locale locale) throws ParseException {
        
        Double v = new DecimalFormat("#,##0.00").parse(string).doubleValue();
        return v;
    }
}
