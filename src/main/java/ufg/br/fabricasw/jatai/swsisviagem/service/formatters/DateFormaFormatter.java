package ufg.br.fabricasw.jatai.swsisviagem.service.formatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

/**
 * Classe para formatar a data em PT_BR.
 *
 * @author Ronaldo N. de Sousa
 */
@Service
public class DateFormaFormatter implements Formatter<LocalDate> {

    @Override
    public String print(LocalDate object, Locale locale) {

        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return object.format(ofPattern);
    }

    /**
     * Espera receber data no seguinte formato: yyyy-MM-dd -> 2018-09-30
     * @param text
     * @param locale
     * @return 
     */
    @Override
    public LocalDate parse(String text, Locale locale) {
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(text, ofPattern);
    }
}
