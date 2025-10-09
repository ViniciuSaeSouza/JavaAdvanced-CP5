package consertaJa.ConsertaJa.annotations.interfaces;

import consertaJa.ConsertaJa.annotations.clazz.CnpjValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CnpjValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CNPJ {

    String message() default "CNPJ inválido"; // mensagem padrão

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

