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
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.AdicionadorLinkVenda;
import com.autobots.automanager.modelo.VendaAtualizador;
import com.autobots.automanager.repositorios.VendaRepositorio;

@RestController("/vendas")
public class VendaControle {
	
	@Autowired
	private VendaRepositorio repositorio;

	@Autowired
	private AdicionadorLinkVenda adicionadorLink;
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@GetMapping
	public ResponseEntity<List<Venda>> obterVendas() {
		List<Venda> vendas = repositorio.findAll();
		if (vendas.isEmpty()) {
			ResponseEntity<List<Venda>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(vendas);
			ResponseEntity<List<Venda>> resposta = new ResponseEntity<>(vendas, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
	@GetMapping("/{idVenda}")
	public ResponseEntity<?> buscarVendaPorId(@PathVariable("idVenda") Long idVenda) {
		Venda venda = repositorio.findById(idVenda).get();
		if(venda == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada");
		}
		adicionadorLink.adicionarLink(venda);
		return ResponseEntity.ok(venda);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PostMapping("/adicionarVenda")
	public ResponseEntity<Object> cadastrarVenda(@RequestBody Venda venda) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (venda.getId() == null) {
			repositorio.save(venda);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@PutMapping("/atualizarVenda")
	public ResponseEntity<?> atualizarVenda(@RequestBody Venda atualizacao) {		
		HttpStatus status = HttpStatus.CONFLICT;
		Venda venda = repositorio.findById(atualizacao.getId()).get();
		if (venda != null) {
			VendaAtualizador atualizador = new VendaAtualizador();
			atualizador.atualizar(venda, atualizacao);
			repositorio.save(venda);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@DeleteMapping("/{idVenda}")
	public ResponseEntity<?> excluirVenda(@PathVariable("idVenda") long idVenda) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Venda venda = repositorio.findById(idVenda).get();
		if (venda != null) {
			repositorio.delete(venda);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}
