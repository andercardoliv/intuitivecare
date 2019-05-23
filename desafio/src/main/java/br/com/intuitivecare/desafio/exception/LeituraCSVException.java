package br.com.intuitivecare.desafio.exception;

public class LeituraCSVException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LeituraCSVException(String mensagem) {
		super(mensagem);
	}
	
	public LeituraCSVException(String mensagem, Throwable err) {
		super(mensagem, err);
	}
}
