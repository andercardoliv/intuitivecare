package pdfreader;

import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.measurement.PDViewportDictionary;
import org.apache.pdfbox.pdmodel.interactive.pagenavigation.PDThreadBead;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

public class PDFReaderTest {
	
	public static void main(String[] args) throws IOException {
		
		try (PDDocument document = PDDocument.load(new File(
				"C:\\Users\\Anderson\\projetos\\padrao_tiss_componente_organizacional_201902.pdf"))) {
			
			PDFTextStripper stripper = new PDFTextStripper();
			
			Pattern pattern = Pattern.compile(
					"Quadro\\s\\d+\\s[–|-]+.+?[\r*\n](.*?)Fonte:", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			
			Matcher matcher = pattern.matcher(stripper.getText(document));
			
			//Pegando todas as tabelas com o padrão "Quadro [nº] - xxxx" (padrão do doc) e colocando em uma lista
			List<String> tabelas = new ArrayList<>();
			while (matcher.find()) {
//				System.out.println(matcher.group(1));
				tabelas.add(matcher.group(1));
			}
			
			//pegando tabela do quadro 31
			String tbCategoriaPadraoTISS = tabelas.get(30);

			//Replace do texto do rodapé das páginas.
			tbCategoriaPadraoTISS = tbCategoriaPadraoTISS.replaceAll("\\d+\\s*\r*\nPadrão.*?\\s*\r*\n", "");
			
			//Pegando cada registro da tabela via Pattern
			Pattern patternNumberText = Pattern.compile(
					"(\\d+\\s*\r*\n*\\w+.*?[\r*\n.*?[^\\d]]+)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			
			//Preenchendo lista com os registros da tabela para gravar em CSV e subir para o banco.
			List<String> linhasCategoriaPadrao = new ArrayList<>();
			
			Matcher matcherNumberText = patternNumberText.matcher(tbCategoriaPadraoTISS);
			while (matcherNumberText.find()) {
				linhasCategoriaPadrao.add(matcherNumberText.group(1).replaceAll("\r*\n", "")
						.replaceFirst(" ", ","));
				
			}
			
			//Escrevendo arquivo CSV
			String cabecalho = tbCategoriaPadraoTISS.split("\r*\n")[2].replaceFirst(" ", ",");
			
			Path path = Paths.get("teste.csv");
			try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				writer.write(cabecalho);
				
				linhasCategoriaPadrao.forEach(linha -> {
					try {
						writer.write("\n");
						writer.write(linha);
					} catch(IOException e) {
						System.out.println(e.getMessage());
					}
				});
			} finally {
				System.out.println("Fim do processo");
			}
			
			//Colocando arquivo dentro do zip
			try(ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(Paths.get("teste.zip").toFile()))) {
				zos.putNextEntry(new ZipEntry(path.toFile().getName()));
				Files.copy(path, zos);
			}
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
}
