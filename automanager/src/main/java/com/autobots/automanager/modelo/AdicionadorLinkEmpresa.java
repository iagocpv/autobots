package com.autobots.automanager.modelo;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entidades.Empresa;

@Component
public class AdicionadorLinkEmpresa implements AdicionadorLink<Empresa>{

	@Override
	public void adicionarLink(List<Empresa> lista) {
		for (Empresa empresa : lista) {
			this.adicionarLink(empresa);
		}
	}

	@Override
	public void adicionarLink(Empresa objeto) {
		long id = objeto.getId();
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EmpresaControle.class)
						.buscarEmpresaPorId(id))
				.withSelfRel();
		objeto.add(linkProprio);
		
		Link linkListar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EmpresaControle.class)
						.buscarEmpresas())
				.withRel("empresas");
		objeto.add(linkListar);
		
		Link linkCadastrar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EmpresaControle.class)
						.cadastrarEmpresa(objeto))
				.withRel("cadastrar");
		objeto.add(linkCadastrar);
		
		Link linkEditar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EmpresaControle.class)
						.atualizarEmpresa(objeto))
				.withRel("editar");
		objeto.add(linkEditar);
		
		Link linkDeletar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EmpresaControle.class)
						.excluirEmpresa(id))
				.withRel("deletar");
		objeto.add(linkDeletar);
	}

}
