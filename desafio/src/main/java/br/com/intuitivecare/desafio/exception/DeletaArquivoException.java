package br.com.intuitivecare.desafio.exception;

public class DeletaArquivoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DeletaArquivoException(String mensagem) {
		super(mensagem);
	}
	
	public DeletaArquivoException(String mensagem, Throwable err) {
		super(mensagem, err);
	}
}
