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

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelo.AdicionadorLinkServico;
import com.autobots.automanager.modelo.ServicoAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;

@RestController
public class ServicoControle {
	
	@Autowired
	private EmpresaRepositorio repositorioEmpresa;
	@Autowired
	private ServicoRepositorio repositorioServico;
	@Autowired 
	private ServicoAtualizador atualizador;
	@Autowired
	private AdicionadorLinkServico adicionadorLink;
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("/servicos/buscarServicoPorEmpresa/{idEmpresa}")
	public ResponseEntity<?> buscarServicosPorEmpresa(@PathVariable("idEmpresa") Long idEmpresa) {
		Empresa empresa = repositorioEmpresa.findById(idEmpresa).get();
		if(empresa == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
		}
		return ResponseEntity.ok(empresa.getServicos());
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("/servicos/")
	public ResponseEntity<List<Servico>> obterServicos() {
		List<Servico> servicos = repositorioServico.findAll();
		if (servicos.isEmpty()) {
			ResponseEntity<List<Servico>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(servicos);
			ResponseEntity<List<Servico>> resposta = new ResponseEntity<>(servicos, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("/servicos/{idServico}")
	public ResponseEntity<?> buscarServicoPorId(@PathVariable("idServico") Long idServico) {
		Servico servico = repositorioServico.findById(idServico).get();
		if(servico == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado");
		}
		adicionadorLink.adicionarLink(servico);
		return ResponseEntity.ok(servico);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@PostMapping("/servicos/adicionarServico/{idEmpresa}")
	public ResponseEntity<Object> adicionarServico(@PathVariable("idEmpresa") Long idEmpresa, @RequestBody Servico servico) {
		Empresa empresa = repositorioEmpresa.findById(idEmpresa).get();
		if(empresa == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
		}
		empresa.getServicos().add(servico);
		repositorioEmpresa.save(empresa);
		return ResponseEntity.ok("Serviço adicionado");
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@DeleteMapping("/servicos/{idServico}")
	public ResponseEntity<?> excluirServico(@PathVariable("idServico") Long idServico) {
		Servico servico = repositorioServico.findById(idServico).get();
		if(servico == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado");
		}
		repositorioServico.delete(servico);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Serviço excluído");
	}
	
	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@PutMapping("/servicos/{idServico}")
	public ResponseEntity<?> atualizarServico(@PathVariable("idServico") Long idServico, @RequestBody Servico atualizacao) {
		Servico servico = repositorioServico.findById(idServico).get();
		if(servico == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado");
		}
		atualizador.atualizar(servico, atualizacao);
		repositorioServico.save(servico);
		return ResponseEntity.ok("Servico atualizado");
	}
		

}
