package ufg.br.fabricasw.jatai.swsisviagem.domain.util.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Verifica se há um arquivo inserido.
 * @author ronogue
 */
@Documented
@Constraint(validatedBy = MultipartFileNotEmptyValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultipartFileNotEmpty {

    String message() default "Um arquivo deve ser selecionado.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
}
