package br.com.intuitivecare.desafio.controller;

import static br.com.intuitivecare.desafio.util.PDFConstants.PATTERN_COMPONENTE_ORG;
import static br.com.intuitivecare.desafio.util.PDFConstants.PATTERN_LINHA_TABELA_CATEGORIA_PADRAO;
import static br.com.intuitivecare.desafio.util.PDFConstants.PATTERN_RODAPE_PAGINA;
import static br.com.intuitivecare.desafio.util.PDFConstants.PATTERN_TABELA_COMPONENTE_ORG;

import java.io.File;
import java.util.List;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.intuitivecare.desafio.model.CategoriaPadrao;
import br.com.intuitivecare.desafio.service.CategoriaPadraoService;
import br.com.intuitivecare.desafio.util.CSVUtil;
import br.com.intuitivecare.desafio.util.PDFUtil;

@Scope(value = "session")
@Component(value = "categoriaPadraoController")
@ELBeanName(value = "categoriaPadraoController")
@Join(path = "/", to = "categoriapadrao.jsf")
@Configuration
@PropertySource("classpath:application.properties")
public class CategoriaPadraoController {
	
	@Autowired
	private PDFUtil pdfUtil;
	
	@Autowired
	private CategoriaPadraoService service;
	
	@Autowired
	private CSVUtil csvUtil;
	
	@Value("${indice.tabela.pdf.categoriapadrao}")
	private int indexCategoriaPadrao;
	
	private CategoriaPadrao categoriaPadrao;
	
	private String diretorioZip;
	
	public String processaCategoriaPadrao() {
		
		String urlRetorno = "/categoriapadrao-lista.jsf?faces-redirect=true";
		File pdf = null;
		try {
			pdf = pdfUtil.downloadPDF(PATTERN_COMPONENTE_ORG);
			
			if (pdf != null) {
				List<String> tabelas = pdfUtil.getTabelasFromPDF(
						pdf, PATTERN_TABELA_COMPONENTE_ORG, PATTERN_RODAPE_PAGINA);
				
				if (! tabelas.isEmpty()) {
					String tabelaCategoriaPadrao = tabelas.get(Integer.valueOf(indexCategoriaPadrao));
					
					String regexSeparadorElementos = " ";
					
					String cabecalho = tabelaCategoriaPadrao.split("\r*\n")[2].replaceFirst(regexSeparadorElementos, ","); 
					List<CategoriaPadrao> categoriasPadrao = service.retornaCategoriaPadraoTabela(
							tabelaCategoriaPadrao, PATTERN_LINHA_TABELA_CATEGORIA_PADRAO, regexSeparadorElementos);
					
					service.salvaRegistrosBanco(categoriasPadrao);
					csvUtil.criaZipCSV(categoriasPadrao, cabecalho, diretorioZip);
					
				}
			}
		} catch(Exception e) {
			urlRetorno = "/error.jspf?faces-redirect=true";
			log.error(e.getMessage(), e);
		} finally {
			if (pdf != null) pdfUtil.deleteFile(pdf);
		}
		
		return urlRetorno;
	}
	
	public CategoriaPadrao getCategoriaPadrao() {
		return categoriaPadrao;
	}
	
	public String getDiretorioZip() {
		return this.diretorioZip;
	}
	
	public void setDiretorioZip(String diretorioZip) {
		this.diretorioZip = diretorioZip;
	}
	
	private static final Logger log = LoggerFactory.getLogger(CategoriaPadraoController.class);
}
