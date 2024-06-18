package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.entidades.Cliente;

@Component
public class AdicionadorLinkCliente implements AdicionadorLink<Cliente> {

	@Override
	public void adicionarLink(List<Cliente> lista) {
		for (Cliente cliente : lista) {
			this.adicionarLink(cliente);
		}
	}

	@Override
	public void adicionarLink(Cliente cliente) {
		long id = cliente.getId();
		
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.obterCliente(id))
				.withSelfRel();
		cliente.add(linkProprio);
		
		Link linkCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.cadastrarCliente(cliente))
				.withRel("cadastrar");
		cliente.add(linkCadastrar);
		
		Link linkDelete = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.excluirCliente(id))
				.withRel("deletar");
		cliente.add(linkDelete);
		
		Link linkEditar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.atualizarCliente(cliente))
				.withRel("atualizar");
		cliente.add(linkEditar);
		
		Link linkListar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ClienteControle.class)
						.obterClientes())
				.withRel("clientes");
		cliente.add(linkListar);
	}
}