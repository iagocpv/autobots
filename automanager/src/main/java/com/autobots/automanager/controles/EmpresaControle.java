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
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelo.EmpresaAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;

@RestController
public class EmpresaControle {
	
	@Autowired
	EmpresaRepositorio repositorioEmpresa;
	@Autowired
	AdicionadorLinkEmpresa adicionadorLink;
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/empresas")
	public ResponseEntity<?> buscarEmpresas() {
		List<Empresa> empresas = repositorioEmpresa.findAll();
		if (empresas.isEmpty()) {
			ResponseEntity<List<Empresa>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(empresas);
			ResponseEntity<List<Empresa>> resposta = new ResponseEntity<>(empresas, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/empresas/{idEmpresa}")
	public ResponseEntity<Empresa> buscarEmpresaPorId(@PathVariable("idEmpresa") Long idEmpresa) {
		Empresa empresa = repositorioEmpresa.findById(idEmpresa).get();
		if (empresa == null) {
			ResponseEntity<Empresa> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(empresa);
			ResponseEntity<Empresa> resposta = new ResponseEntity<Empresa>(empresa, HttpStatus.FOUND);
			return resposta;
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping("/empresas/{idEmpresa}")
	public ResponseEntity<Object> excluirEmpresa(@PathVariable("idEmpresa") Long idEmpresa) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Empresa empresa = repositorioEmpresa.findById(idEmpresa).get();
		if (empresa != null) {
			repositorioEmpresa.delete(empresa);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping("/empresas/atualizar")
	public ResponseEntity<Object> atualizarEmpresa(@RequestBody Empresa atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Empresa empresa = repositorioEmpresa.findById(atualizacao.getId()).get();
		if (empresa != null) {
			EmpresaAtualizador atualizador = new EmpresaAtualizador();
			atualizador.atualizar(empresa, atualizacao);
			repositorioEmpresa.save(empresa);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/empresas/cadastrar")
	public ResponseEntity<Object> cadastrarEmpresa(@RequestBody Empresa empresa) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (empresa.getId() == null) {
			repositorioEmpresa.save(empresa);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/empresas/{idEmpresa}/associarUsuario")
	public ResponseEntity<?> associarUsuario(@PathVariable("idEmpresa") Long idEmpresa, @RequestBody Usuario usuario) {
		Empresa empresa = repositorioEmpresa.findById(idEmpresa).get();
		if(empresa == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		empresa.getUsuarios().add(usuario);
		repositorioEmpresa.save(empresa);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
