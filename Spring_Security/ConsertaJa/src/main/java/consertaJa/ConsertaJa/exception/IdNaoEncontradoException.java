package consertaJa.ConsertaJa.exception;

public class IdNaoEncontradoException extends RuntimeException {

    public IdNaoEncontradoException(){
        super("Não foi possível localizar um objeto com este Id");
    }

    public IdNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
