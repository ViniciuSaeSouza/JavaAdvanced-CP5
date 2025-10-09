package consertaJa.ConsertaJa.exception;

public class ValorNegativoException extends Exception{

    public ValorNegativoException(){
        super("Valor inválido, insira um valor positivo");
    }

    public ValorNegativoException(String mensagem){
        super(mensagem);
    }
}