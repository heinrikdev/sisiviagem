package ufg.br.fabricasw.jatai.swsisviagem.domain.util.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * Validaor de input tipo file.
 * @author ronogue
 */
public class MultipartFileNotEmptyValidator implements ConstraintValidator<MultipartFileNotEmpty, MultipartFile>{

    @Override
    public void initialize(MultipartFileNotEmpty constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return value != null && !value.isEmpty();
    }
}
