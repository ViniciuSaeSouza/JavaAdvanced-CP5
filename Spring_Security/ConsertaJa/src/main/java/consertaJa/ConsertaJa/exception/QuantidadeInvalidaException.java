package consertaJa.ConsertaJa.exception;

public class QuantidadeInvalidaException extends Exception{

    public QuantidadeInvalidaException(){
        super("Valor inválido, por favor insira um correto");
    }

    public QuantidadeInvalidaException(String mensagem) {
        super(mensagem);
    }
}
