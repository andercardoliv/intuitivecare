package br.com.intuitivecare.desafio.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.intuitivecare.desafio.exception.ZIPCSVException;
import br.com.intuitivecare.desafio.model.CategoriaPadrao;

public class CSVUtil {
	
	/**
	 * Método que cria o CSV e zipa no diretório escolhido.
	 * Por enquanto este método não está genérico para todas as tabelas do PDF e funciona apenas para a tabela do Quadro 31.
	 * @param tabela String que contém a tabela que será formatada em CSV e zipada
	 * @param diretorioZip Diretório em que o zip será criado
	 * @param regexLinha Expressão regular que pega as linhas da tabela com a String dada.
	 * @throws ZIPCSVException 
	 */
	public void criaZipCSV(List<CategoriaPadrao> categoriasPadrao, String cabecalho, String diretorioZip) throws ZIPCSVException {
		
		//Escrevendo arquivo CSV
		Path pathCSV = Paths.get(CSVConstants.NOME_ARQUIVO + ".csv");
		
			
		//Colocando arquivo dentro do zip
		try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
				Paths.get(diretorioZip + CSVConstants.NOME_ARQUIVO + ".zip").toFile()))) {
			
			zos.putNextEntry(new ZipEntry(pathCSV.toFile().getName()));
			
			zos.write(cabecalho.getBytes());
			categoriasPadrao.forEach(categoriaPadrao -> {
				try {
					zos.write("\n".getBytes());
					zos.write(String.valueOf(categoriaPadrao.getCodigo()).getBytes()); 
					zos.write(",".getBytes());
					zos.write(categoriaPadrao.getDescricao().getBytes());
				} catch(IOException e) {
					System.out.println(e.getMessage());
				}
			});
			
			zos.finish();
			zos.flush();
			zos.closeEntry();
		} catch(IOException e) { 
			log.error(e.getMessage());
			throw new ZIPCSVException(e.getMessage(), e); 
		} 
	}
	
	private static final Logger log = LoggerFactory.getLogger(CSVUtil.class);
}
