package consertaJa.ConsertaJa.annotations.clazz;

import consertaJa.ConsertaJa.annotations.interfaces.CNPJ;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CnpjValidator implements ConstraintValidator<CNPJ, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;

        String digits = value.replaceAll("\\D", "");

        if (digits.length() != 14) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "CNPJ inválido: deve conter 14 dígitos"
            ).addConstraintViolation();
            return false;
        }

        if (!isValidCnpj(digits)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "CNPJ inválido: dígitos verificadores incorretos"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean isValidCnpj(String cnpj) {
        if (cnpj.chars().distinct().count() == 1) return false;

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        String base = cnpj.substring(0, 12);
        String dv = cnpj.substring(12);

        int d1 = calcularDigito(base, pesos1);
        int d2 = calcularDigito(base + d1, pesos2);

        return dv.equals("" + d1 + d2);
    }

    private int calcularDigito(String str, int[] pesos) {
        int soma = 0;
        for (int i = 0; i < str.length(); i++) {
            soma += Character.getNumericValue(str.charAt(i)) * pesos[i];
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }
}
