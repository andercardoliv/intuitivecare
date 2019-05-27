package br.com.intuitivecare.desafio.util;

import static br.com.intuitivecare.desafio.util.PDFConstants.LABEL_VERSAO_RECENTE;
import static br.com.intuitivecare.desafio.util.PDFConstants.PATH_LINKS_DOWNLOAD;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.DOTALL;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import br.com.intuitivecare.desafio.exception.DownloadPDFException;
import br.com.intuitivecare.desafio.exception.ExtracaoTabelaPDFException;

@Configuration
@PropertySource("classpath:application.properties")
public class PDFUtil {
	
	@Value("${link.site.ans}")
	private String linkANS;
	
	@Value("${nome.arquivo.pdf}")
	private String caminhoPDF; 
	
	/**
	 * 
	 * Método que faz o download do pdf do Componente Organizacional no site da ans.
	 * @param caminhoArquivoPDF Caminho que será gravado o arquivo baixado do site da ans.
	 * @throws DownloadPDFException 
	 * 
	 */
	public File downloadPDF(String regexArquivo) throws DownloadPDFException {
		File pdf = null;
		
		try(WebClient client = new WebClient()) {
			pdf = new File(caminhoPDF);
			
			//Impedindo do javascript e css da página serem executados.
			client.getOptions().setJavaScriptEnabled(false);
	        client.getOptions().setCssEnabled(false);
	        
			HtmlPage page = client.getPage(linkANS);
			
			//Navegando para o link da página de downloads da versão mais recente.
			DomElement element = page.getAnchorByText(LABEL_VERSAO_RECENTE);
			HtmlPage pageDownload = element.click();
			
			//Pegando conjunto de links dos arquivos da página.
			List<HtmlAnchor> anchors = pageDownload.getByXPath(PATH_LINKS_DOWNLOAD);
		
			HtmlAnchor pdfAnchor = anchors.stream()
					.filter(anchor -> anchor.getTextContent().matches(regexArquivo)) //Filtrando pelo pattern que traz o arquivo desejado através do texto do link.
					.findFirst()
					.orElse(null);
			
			InputStream is = pdfAnchor.click().getWebResponse().getContentAsStream();
			
			FileUtils.copyInputStreamToFile(is, pdf);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new DownloadPDFException(e.getMessage(), e);
		}
		
		return pdf;
	}
	
	/**
	 * Método que lê o pdf de componente organizacional da ANS e retorna todas as tabelas presentes seguindo o padrão observado
	 * @param pdf PDF que será lido para extração das tabelas
	 * @return Lista com todas as tabelas 
	 * @throws ExtracaoTabelaPDFException
	 */
	public List<String> getTabelasFromPDF(File pdf, String regexPDF, String regexRodape) throws ExtracaoTabelaPDFException {
		List<String> tabelasPDF = new ArrayList<>(); 
		
		try (PDDocument document = PDDocument.load(pdf)) {
			PDFTextStripper stripper = new PDFTextStripper();
			
			Pattern pattern = Pattern.compile(
					regexPDF, DOTALL | CASE_INSENSITIVE);
			
			Matcher matcher = pattern.matcher(stripper.getText(document));
			
			while (matcher.find()) {
				String tabelaExtraida = matcher.group(1);
				tabelasPDF.add(tabelaExtraida.replaceAll(regexRodape, ""));
			}
		} catch(IOException e) {
			log.error(e.getMessage());
			throw new ExtracaoTabelaPDFException(e.getMessage(), e);
		}
		
		return tabelasPDF;
	}
	
	/**
	 * Deleta arquivo pdf
	 * @param pdf Arquivo a ser deletado
	 */
	public void deleteFile(File pdf) {
		try {
			Files.delete(Paths.get(pdf.getAbsolutePath()));
		} catch(IOException e) {
			log.error("Erro ao deletar arquivo pdf baixado: " + e.getMessage(), e);
		}
	}
	
	private static final Logger log = LoggerFactory.getLogger(PDFUtil.class);
}
