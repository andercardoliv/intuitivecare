package br.com.intuitivecare.desafio.util;

public class PDFConstants {

	public static final String LABEL_VERSAO_RECENTE = "Clique aqui para acessar a versão Fevereiro/2019";
	
	public static final String PATH_LINKS_DOWNLOAD = "//a[@class='btn btn-primary btn-sm center-block']";
	
	public static final String PATTERN_COMPONENTE_ORG = ".*?Componente Organizacional.*?pdf.*";
	
	/*
	 * Pattern utilizado para descobrir uma tabela no pdf de componente organizacional:
	 * todo o texto entre a linha em que aparece a string Quadro - [String] até Fonte:
	 */  
	public static final String PATTERN_TABELA_COMPONENTE_ORG = "Quadro\\s\\d+\\s[–|-]+.+?[\r*\n](.*?)Fonte:";
	
	/*
	 * Pattern utilizado para recuperar apenas as linhas com registros da tabela de categoria padrão
	 * Decimal + "espaço" + texto até o próximo decimal aparecer (incluindo quebras de linha).
	 */
	public static final String PATTERN_LINHA_TABELA_CATEGORIA_PADRAO = "(\\d+\\s*\r*\n*\\w+.*?[\r*\n.*?[^\\d]]+)";
	
	/*
	 * pattern utilizado para, uma vez descobertas as tabelas do pdf, retirar o texto de 
	 * rodapé/cabeçalho da página quando a tabela está em mais de uma página.
	 */
	public static final String PATTERN_RODAPE_PAGINA = "\\d+\\s*\r*\nPadrão.*?\\s*\r*\n";
	
}
