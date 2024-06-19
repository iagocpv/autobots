package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelo.AdicionadorLinkMercadoria;
import com.autobots.automanager.modelo.MercadoriaAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;

@RestController
public class MercadoriaControle {
	
	@Autowired
	private EmpresaRepositorio repositorioEmpresa;
	@Autowired
	private MercadoriaRepositorio repositorioMercadoria;
	@Autowired 
	private MercadoriaAtualizador atualizador;
	@Autowired
	private AdicionadorLinkMercadoria adicionadorLink;
	
	@GetMapping("/mercadorias/buscarMercadoriasPorEmpresa/{idEmpresa}")
	public ResponseEntity<?> buscarMercadoriasPorEmpresa(@PathVariable("idEmpresa") Long idEmpresa) {
		Empresa empresa = repositorioEmpresa.findById(idEmpresa).get();
		if(empresa == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
		}
		return ResponseEntity.ok(empresa.getMercadorias());
	}
	
	@GetMapping("/mercadorias/")
	public ResponseEntity<List<Mercadoria>> obterMercadorias() {
		List<Mercadoria> mercadorias = repositorioMercadoria.findAll();
		if (mercadorias.isEmpty()) {
			ResponseEntity<List<Mercadoria>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(mercadorias);
			ResponseEntity<List<Mercadoria>> resposta = new ResponseEntity<>(mercadorias, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@GetMapping("/mercadorias/{idMercadoria}")
	public ResponseEntity<?> buscarMercadoriaPorId(@PathVariable("idMercadoria") Long idMercadoria) {
		Mercadoria mercadoria = repositorioMercadoria.findById(idMercadoria).get();
		if(mercadoria == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mercadoria não encontrada");
		}
		adicionadorLink.adicionarLink(mercadoria);
		return ResponseEntity.ok(mercadoria);
	}
	
	@PostMapping("/mercadorias/adicionarMercadoria/{idEmpresa}")
	public ResponseEntity<Object> adicionarMercadoria(@PathVariable("idEmpresa") Long idEmpresa, @RequestBody Mercadoria mercadoria) {
		Empresa empresa = repositorioEmpresa.findById(idEmpresa).get();
		if(empresa == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
		}
		empresa.getMercadorias().add(mercadoria);
		repositorioEmpresa.save(empresa);
		return ResponseEntity.ok("Mercadoria adicionada");
	}
	
	@DeleteMapping("/mercadorias/{idMercadoria}")
	public ResponseEntity<?> excluirMercadoria(@PathVariable("idMercadoria") Long idMercadoria) {
		Mercadoria mercadoria = repositorioMercadoria.findById(idMercadoria).get();
		if(mercadoria == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mercadoria não encontrada");
		}
		repositorioMercadoria.delete(mercadoria);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Mercadoria excluída");
	}
	
	@PutMapping("/mercadorias/{idMercadoria}")
	public ResponseEntity<?> atualizarMercadoria(@PathVariable("idMercadoria") Long idMercadoria, @RequestBody Mercadoria atualizacao) {
		Mercadoria mercadoria = repositorioMercadoria.findById(idMercadoria).get();
		if(mercadoria == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mercadoria não encontrada");
		}
		atualizador.atualizar(mercadoria, atualizacao);
		repositorioMercadoria.save(mercadoria);
		return ResponseEntity.ok("Mercadoria atualizada");
	}
		

}
