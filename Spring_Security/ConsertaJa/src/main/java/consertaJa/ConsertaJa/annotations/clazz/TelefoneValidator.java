package consertaJa.ConsertaJa.annotations.clazz;

import consertaJa.ConsertaJa.annotations.interfaces.Telefone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneValidator implements ConstraintValidator<Telefone, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;

        String digits = value.replaceAll("\\D", "");

        if (digits.length() != 10 && digits.length() != 11) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Telefone inválido: deve ter 10 dígitos (fixo) ou 11 dígitos (celular)"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}