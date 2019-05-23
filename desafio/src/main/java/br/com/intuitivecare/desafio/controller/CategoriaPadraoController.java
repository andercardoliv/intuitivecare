package br.com.intuitivecare.desafio.controller;

import java.util.List;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.intuitivecare.desafio.model.CategoriaPadrao;
import br.com.intuitivecare.desafio.repository.CategoriaPadraoRepository;
import br.com.intuitivecare.desafio.util.CSVUtil;

@Scope(value = "session")
@Component(value = "categoriaPadraoController")
@ELBeanName(value = "categoriaPadraoController")
@Join(path = "/", to = "categoriapadrao.jsf")
public class CategoriaPadraoController {

	@Autowired
	private CategoriaPadraoRepository categoriaPadraoRepository;
	
	@Autowired
	private CSVUtil csvUtil;
	
	private CategoriaPadrao categoriaPadrao;
	
	private String diretorioCSV;
	
	private Logger log;
	
	public String importaCSV() {
		String urlRetorno = "/categoriapadrao-lista.jsf?redirect=true";
		
		try {
			csvUtil.descompactaArquivo(diretorioCSV);
			List<CategoriaPadrao> categoriasPadrao = csvUtil.getCategoriasFromCSV();
			
			//Adicionando categorias no banco.
			categoriasPadrao.forEach(categoria -> {
				Integer codigo = categoriaPadraoRepository.findByCodigo(categoria.getCodigo());
				if (codigo == null) {
					categoriaPadraoRepository.save(categoria);
				}
			});
			
			csvUtil.deletaArquivo();
			
		} catch(Exception e) {
			urlRetorno = "/erro.jsf";
			log.error(e.getMessage(), e);
		}
		
		return urlRetorno;
	}
	
	public CategoriaPadrao getCategoriaPadrao() {
		return categoriaPadrao;
	}
	
	public String getDiretorioCSV() {
		return this.diretorioCSV;
	}
	
	public void setDiretorioCSV(String diretorioCSV) {
		this.diretorioCSV = diretorioCSV;
	}
}
