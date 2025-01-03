package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelo.AdicionadorLinkVeiculo;
import com.autobots.automanager.modelo.VeiculoAtualizador;
import com.autobots.automanager.repositorios.VeiculoRepositorio;

public class VeiculoControle {
	
	@Autowired
	private VeiculoRepositorio repositorioVeiculo;
	@Autowired
	private AdicionadorLinkVeiculo adicionadorLink;
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@GetMapping("/veiculos")
	public ResponseEntity<List<Veiculo>> obterVeiculos() {
		List<Veiculo> veiculos = repositorioVeiculo.findAll();
		if (veiculos.isEmpty()) {
			ResponseEntity<List<Veiculo>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(veiculos);
			ResponseEntity<List<Veiculo>> resposta = new ResponseEntity<>(veiculos, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@GetMapping("/veiculos/{idVeiculo}")
	public ResponseEntity<?> buscarVeiculoPorId(@PathVariable("idVeiculo") Long idVeiculo) {
		Veiculo veiculo = repositorioVeiculo.findById(idVeiculo).get();
		if(veiculo == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veículo não encontrado");
		}
		adicionadorLink.adicionarLink(veiculo);
		return ResponseEntity.ok(veiculo);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@PostMapping("/veiculos/adicionarVeiculo")
	public ResponseEntity<Object> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (veiculo.getId() == null) {
			repositorioVeiculo.save(veiculo);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@PutMapping("/veiculos/atualizar")
	public ResponseEntity<?> atualizarVeiculo(@RequestBody Veiculo atualizacao) {		
		HttpStatus status = HttpStatus.CONFLICT;
		Veiculo veiculo = repositorioVeiculo.findById(atualizacao.getId()).get();
		if (veiculo != null) {
			VeiculoAtualizador atualizador = new VeiculoAtualizador();
			atualizador.atualizar(veiculo, atualizacao);
			repositorioVeiculo.save(veiculo);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@DeleteMapping("/veiculos/{idVeiculo}")
	public ResponseEntity<?> excluirVeiculo(@PathVariable("idVeiculo") long idVeiculo) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Veiculo veiculo = repositorioVeiculo.findById(idVeiculo).get();
		if (veiculo != null) {
			repositorioVeiculo.delete(veiculo);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}

}
