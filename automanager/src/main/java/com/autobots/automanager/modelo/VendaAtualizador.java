package com.autobots.automanager.modelo;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Venda;

@Component
public class VendaAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	private void atualizarDados(Venda venda, Venda atualizacao) {
		if (!verificador.verificar(atualizacao.getIdentificacao())) {
			venda.setIdentificacao(atualizacao.getIdentificacao());
		}
		if (atualizacao.getCadastro() != null) {
			venda.setCadastro(atualizacao.getCadastro());
		}
		if (atualizacao.getCliente() != null) {
			venda.setCliente(atualizacao.getCliente());
		}
		if (atualizacao.getVeiculo() != null) {
			venda.setVeiculo(atualizacao.getVeiculo());
		}
		if (atualizacao.getFuncionario() != null) {
			venda.setFuncionario(atualizacao.getFuncionario());
		}
		if (atualizacao.getMercadorias() != null) {
			venda.setMercadorias(atualizacao.getMercadorias());
		}
		if (atualizacao.getServicos() != null) {
			venda.setServicos(atualizacao.getServicos());
		}
	}

	public void atualizar(Venda venda, Venda atualizacao) {
		atualizarDados(venda, atualizacao);
	}

}
