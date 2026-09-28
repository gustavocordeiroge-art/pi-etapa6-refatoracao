package br.com.clinica.exception;

/** Erro de regra de negócio. A camada de interface (web) decide como exibi-lo. */
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
