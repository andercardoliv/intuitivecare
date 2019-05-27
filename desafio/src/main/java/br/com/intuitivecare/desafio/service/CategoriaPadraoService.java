package br.com.intuitivecare.desafio.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.intuitivecare.desafio.model.CategoriaPadrao;
import br.com.intuitivecare.desafio.repository.CategoriaPadraoRepository;

@Service
public class CategoriaPadraoService {

	@Autowired
	private CategoriaPadraoRepository repository;
	
	public List<CategoriaPadrao> retornaCategoriaPadraoTabela(
			String tabela, String regexLinha, String regexSeparadorElemento) {
		
		List<String> linhasTabela = getLinhasTabela(tabela, regexLinha);
		
		return linhasTabela.stream().map(linha -> {
			String[] arrLinha = linha.split(regexSeparadorElemento, 2);
			return new CategoriaPadrao(Integer.valueOf(arrLinha[0]), arrLinha[1]);
		}).collect(Collectors.toList());
	}
	
	public void salvaRegistrosBanco(List<CategoriaPadrao> categoriasPadrao) {
		categoriasPadrao.forEach(categoria -> {
			Integer codigo = repository.findByCodigo(categoria.getCodigo());
			if (codigo == null) {
				repository.save(categoria);
			}
		});
	}
	
	private List<String> getLinhasTabela(String tabela, String regexLinha) {
		List<String> linhasTabela = new ArrayList<>();
		
		//Pegando cada registro da tabela via Pattern
		Pattern pattern = Pattern.compile(regexLinha, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		
		Matcher matcher = pattern.matcher(tabela);
		while (matcher.find()) {
			linhasTabela.add(matcher.group(1).replaceAll("\r*\n", ""));
		}
		
		return linhasTabela;
	}
}
