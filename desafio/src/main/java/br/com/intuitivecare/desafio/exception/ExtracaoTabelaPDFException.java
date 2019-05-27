package br.com.intuitivecare.desafio.exception;

public class ExtracaoTabelaPDFException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExtracaoTabelaPDFException(String mensagem) {
		super(mensagem);
	}
	
	public ExtracaoTabelaPDFException(String mensagem, Throwable err) {
		super(mensagem, err);
	}
}
