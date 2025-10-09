package consertaJa.ConsertaJa.annotations.interfaces;

import consertaJa.ConsertaJa.annotations.clazz.UniqueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {

    String message() default "Valor já existe";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String fieldName();       // Campo a ser validado
    Class<?> domainClass();   // Classe da entidade

    // Novo parâmetro para ignorar o ID atual
    String ignoreIdField() default "id"; // nome do campo que representa o ID
}
