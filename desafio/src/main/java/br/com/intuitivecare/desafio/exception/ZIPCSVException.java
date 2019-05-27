package br.com.intuitivecare.desafio.exception;

public class ZIPCSVException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ZIPCSVException(String mensagem) {
		super(mensagem);
	}
	
	public ZIPCSVException(String mensagem, Throwable err) {
		super(mensagem, err);
	}
}
