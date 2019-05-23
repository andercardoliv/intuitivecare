package br.com.intuitivecare.desafio.exception;

public class DescompactaArquivoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DescompactaArquivoException(String mensagem) {
		super(mensagem);
	}
	
	public DescompactaArquivoException(String mensagem, Throwable err) {
		super(mensagem, err);
	}
}
