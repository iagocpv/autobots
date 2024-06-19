package com.autobots.automanager.modelo;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entidades.Veiculo;

@Component
public class VeiculoAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	private void atualizarDados(Veiculo veiculo, Veiculo atualizacao) {
		if (!verificador.verificar(atualizacao.getModelo())) {
			veiculo.setModelo(atualizacao.getModelo());
		}
		if (!verificador.verificar(atualizacao.getPlaca())) {
			veiculo.setPlaca(atualizacao.getPlaca());
		}
		if (atualizacao.getTipo() != null) {
			veiculo.setTipo(atualizacao.getTipo());
		}
		if (atualizacao.getProprietario() != null) {
			veiculo.setProprietario(atualizacao.getProprietario());
		}
	}

	public void atualizar(Veiculo veiculo, Veiculo atualizacao) {
		atualizarDados(veiculo, atualizacao);
	}
}
