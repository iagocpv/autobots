package com.autobots.automanager.modelo;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Mercadoria;

@Component
public class MercadoriaAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();
	
	public void atualizar(Mercadoria mercadoria, Mercadoria atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(atualizacao.getNome())) {
				mercadoria.setNome(atualizacao.getNome());
			}
			if (atualizacao.getValor() != null) {
				mercadoria.setValor(atualizacao.getValor());
			}
			if (!verificador.verificar(atualizacao.getDescricao())) {
				mercadoria.setDescricao(atualizacao.getDescricao());
			}
			if(atualizacao.getFabricao() != null) {
				mercadoria.setFabricao(atualizacao.getFabricao());
			}
			if(atualizacao.getValidade() != null) {
				mercadoria.setValidade(atualizacao.getValidade());
			}
			if(atualizacao.getQuantidade() != null) {
				mercadoria.setQuantidade(atualizacao.getQuantidade());
			}
		}
	}
	
}
