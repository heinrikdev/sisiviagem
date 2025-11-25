package ufg.br.fabricasw.jatai.swsisviagem.domain.util.validators;

import java.time.LocalDate;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Valida se há uma data inserida.
 * @author Ronaldo N. de Sousa
 */
public class CustomLocalDateValidator implements ConstraintValidator<CustomLocalDateNotEmpty, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value != null;
    }
}
