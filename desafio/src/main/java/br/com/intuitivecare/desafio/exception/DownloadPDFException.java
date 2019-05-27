package br.com.intuitivecare.desafio.exception;

public class DownloadPDFException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DownloadPDFException(String mensagem) {
		super(mensagem);
	}
	
	public DownloadPDFException(String mensagem, Throwable err) {
		super(mensagem, err);
	}
}
