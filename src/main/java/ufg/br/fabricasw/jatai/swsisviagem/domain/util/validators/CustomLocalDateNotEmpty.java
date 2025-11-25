package ufg.br.fabricasw.jatai.swsisviagem.domain.util.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Documented
@Constraint(validatedBy = CustomLocalDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomLocalDateNotEmpty {

    String message() default "Uma data deve ser inserida!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
