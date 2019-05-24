package br.com.intuitivecare.desafio.util;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.ResourceUtils;

import com.opencsv.CSVReader;

import br.com.intuitivecare.desafio.exception.DeletaArquivoException;
import br.com.intuitivecare.desafio.exception.DescompactaArquivoException;
import br.com.intuitivecare.desafio.exception.LeituraCSVException;
import br.com.intuitivecare.desafio.model.CategoriaPadrao;

@Configuration
@PropertySource("classpath:application.properties")
public class CSVUtil {

	@Autowired
	private Environment env;
	
	public void descompactaArquivo(String caminhoArquivo) throws DescompactaArquivoException {
		try (ZipInputStream zipIS = new ZipInputStream(
				new FileInputStream(getZip(caminhoArquivo).toAbsolutePath().toString()))) {
			
			ZipEntry arquivoZip = zipIS.getNextEntry();
			while (arquivoZip != null) {
				Path caminhoDescompactacao = Paths.get("", env.getProperty("csv.filename"));
				escreveArquivo(zipIS, caminhoDescompactacao);
				
				zipIS.closeEntry();
				arquivoZip = zipIS.getNextEntry();
			}
			
		} catch(IOException e) {
			log.error(e.getMessage(), e);
			throw new DescompactaArquivoException(e.getMessage(), e);
		}
	}
	
	public List<CategoriaPadrao> getCategoriasFromCSV() throws LeituraCSVException {
		
		List<CategoriaPadrao> categorias = null;
		try (CSVReader reader = new CSVReader(new InputStreamReader(
				new FileInputStream(env.getProperty("csv.filename"))))) {
			
			List<String[]> linhas = reader.readAll();
			
			categorias = linhas.stream()
					.map(linha -> new CategoriaPadrao(Integer.valueOf(linha[0]), linha[1]))
					.collect(Collectors.toList());
			
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new LeituraCSVException(e.getMessage(), e);
		}
		
		return categorias;
	}
	
	public void deletaArquivo() throws DeletaArquivoException {
		try {
			Files.delete(Paths.get(env.getProperty("csv.filename")));
		} catch(IOException e) {
			log.error(e.getMessage(), e);
			throw new DeletaArquivoException(e.getMessage(), e);
		}
	}
	
	private Path getZip(String caminhoArquivo) throws IOException {
		Path path = null;
		if (caminhoArquivo != null && !caminhoArquivo.isEmpty()) 
			path = Paths.get(caminhoArquivo);
		else 
			path = Paths.get(ResourceUtils.getFile("classpath:" +  new String(env.getProperty("zip.filename").getBytes(),"UTF-8")).getPath());
			
		return path;
	}
	
	private void escreveArquivo(ZipInputStream zipIS, Path filePath) throws IOException {
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath.toAbsolutePath().toString()))) {
            byte[] bytesIn = new byte[1024];
            int read = 0;
            while ((read = zipIS.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
	}
	
	private static final Logger log = LoggerFactory.getLogger(CSVUtil.class);
}
