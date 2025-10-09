package consertaJa.ConsertaJa.utils;

public class FormatUtils {

    public static String formatCnpj(String cnpj) {
        if (cnpj == null) return null;

        String digits = cnpj.replaceAll("\\D", "");

        if (digits.length() != 14) {
            throw new IllegalArgumentException("CNPJ inválido");
        }

        return digits.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                "$1.$2.$3/$4-$5");
    }

    public static String formatTelefone(String telefone) {
        if (telefone == null) return null;

        String digits = telefone.replaceAll("\\D", "");

        if (digits.length() == 10) {
            return digits.replaceFirst("(\\d{2})(\\d{4})(\\d{4})",
                    "($1) $2-$3");
        } else if (digits.length() == 11) {
            return digits.replaceFirst("(\\d{2})(\\d{5})(\\d{4})",
                    "($1) $2-$3");
        } else {
            throw new IllegalArgumentException("Telefone inválido");
        }
    }
}