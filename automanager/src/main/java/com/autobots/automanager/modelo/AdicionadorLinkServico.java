package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.entidades.Servico;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico>{
	@Override
	public void adicionarLink(List<Servico> lista) {
		for (Servico servico : lista) {
			this.adicionarLink(servico);
		}
	}

	@Override
	public void adicionarLink(Servico objeto) {
		long id = objeto.getId();
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ServicoControle.class)
						.buscarServicoPorId(id))
				.withSelfRel();
		objeto.add(linkProprio);
		
		Link linkListar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ServicoControle.class)
						.obterServicos())
				.withRel("servicos");
		objeto.add(linkListar);
		
		Link linkEditar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ServicoControle.class)
						.atualizarServico(id, objeto))
				.withRel("editar");
		objeto.add(linkEditar);
		
		Link linkDeletar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ServicoControle.class)
						.excluirServico(id))
				.withRel("deletar");
		objeto.add(linkDeletar);
	}
}
