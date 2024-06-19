package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.entidades.Venda;

@Component
public class AdicionadorLinkVenda implements AdicionadorLink<Venda> {
	
	@Override
	public void adicionarLink(List<Venda> lista) {
		for (Venda venda : lista) {
			this.adicionarLink(venda);
		}
	}

	@Override
	public void adicionarLink(Venda objeto) {
		long id = objeto.getId();
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VendaControle.class)
						.buscarVendaPorId(id))
				.withSelfRel();
		objeto.add(linkProprio);
		
		Link linkListar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VendaControle.class)
						.obterVendas())
				.withRel("veiculos");
		objeto.add(linkListar);
		
		Link linkCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VendaControle.class)
						.cadastrarVenda(objeto))
				.withRel("cadastrar");
		objeto.add(linkCadastrar);
		
		Link linkEditar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VendaControle.class)
						.atualizarVenda(objeto))
				.withRel("editar");
		objeto.add(linkEditar);
		
		Link linkDeletar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VendaControle.class)
						.excluirVenda(id))
				.withRel("deletar");
		objeto.add(linkDeletar);
	}

}
