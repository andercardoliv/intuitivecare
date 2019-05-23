package br.com.intuitivecare.desafio.controller;

import java.util.List;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.intuitivecare.desafio.model.CategoriaPadrao;
import br.com.intuitivecare.desafio.repository.CategoriaPadraoRepository;

@Scope (value = "session")
@Component (value = "categoriaPadraoLista")
@ELBeanName(value = "categoriaPadraoLista")
@Join(path = "/lista", to = "/categoriapadrao-lista.jsf")
public class CategoriaPadraoListaController {

	@Autowired
	private CategoriaPadraoRepository categoriaPadraoRepository;
	
	private List<CategoriaPadrao> categoriasPadrao;
	
	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		categoriasPadrao = categoriaPadraoRepository.findAll();
	}
	
	public List<CategoriaPadrao> getCategoriasPadrao() {
		return categoriasPadrao;
	}
	
}
