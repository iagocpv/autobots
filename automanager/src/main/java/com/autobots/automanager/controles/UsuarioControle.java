package com.autobots.automanager.controles;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.AdicionadorLinkUsuario;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.UsuarioAtualizador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControle {
	@Autowired
	private UsuarioRepositorio repositorio;
	@Autowired
	private TelefoneAtualizador telefoneAtualizador;
	@Autowired
	private DocumentoAtualizador documentoAtualizador;
	@Autowired
	private AdicionadorLinkUsuario adicionadorLink;

	@GetMapping("/{idUsuario}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable("idUsuario") long idUsuario) {
		Usuario usuario = repositorio.findById(idUsuario).get();
		if (usuario == null) {
			ResponseEntity<Usuario> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(usuario);
			ResponseEntity<Usuario> resposta = new ResponseEntity<Usuario>(usuario, HttpStatus.FOUND);
			return resposta;
		}
	}

	@GetMapping
	public ResponseEntity<List<Usuario>> obterUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		if (usuarios.isEmpty()) {
			ResponseEntity<List<Usuario>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(usuarios);
			ResponseEntity<List<Usuario>> resposta = new ResponseEntity<>(usuarios, HttpStatus.FOUND);
			return resposta;
		}
	}

	@PostMapping("/cadastrar")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (usuario.getId() == null) {
			repositorio.save(usuario);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario atualizacao) {		
		HttpStatus status = HttpStatus.CONFLICT;
		Usuario usuario = repositorio.findById(atualizacao.getId()).get();
		if (usuario != null) {
			UsuarioAtualizador atualizador = new UsuarioAtualizador();
			atualizador.atualizar(usuario, atualizacao);
			repositorio.save(usuario);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/{idUsuario}")
	public ResponseEntity<?> excluirUsuario(@PathVariable("idUsuario") long idUsuario) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Usuario usuario = repositorio.findById(idUsuario).get();
		if (usuario != null) {
			repositorio.delete(usuario);
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
	
	@PostMapping("/{idUsuario}/adicionarTelefones")
	public Set<Telefone> adicionarTelefones(@PathVariable("idUsuario") Long idUsuario, @RequestBody Set<Telefone> telefones) {
		Usuario usuario = repositorio.findById(idUsuario).get();
		telefones.forEach(tel -> usuario.getTelefones().add(tel));
		return repositorio.save(usuario).getTelefones();
	}
	
	@GetMapping("/{idUsuario}/listarTelefones")
	public Set<Telefone> listarTelefones(@PathVariable("idUsuario") Long idUsuario) {
		return repositorio.getById(idUsuario).getTelefones();
	}
	
	@PutMapping("/{idUsuario}/atualizarTelefones")
	public Set<Telefone> atualizarTelefones(@PathVariable("idUsuario") Long idUsuario, @RequestBody Set<Telefone> telefones) {
		Usuario usuario = repositorio.findById(idUsuario).get();
		telefoneAtualizador.atualizar(usuario.getTelefones(), telefones);
		return repositorio.save(usuario).getTelefones();
	}
	
	@DeleteMapping("/{idUsuario}/excluirTelefones")
	public Set<Telefone> excluirTelefones(@PathVariable("idUsuario") Long idUsuario, @RequestBody Set<Long> idsTelefones) {
		Usuario usuario = repositorio.findById(idUsuario).get();
		usuario.setTelefones(usuario.getTelefones().stream().filter(tel -> !idsTelefones.contains(tel.getId())).collect(Collectors.toSet()));
		return repositorio.save(usuario).getTelefones();
	}
	
	@GetMapping("/{idUsuario}/listarDocumentos")
	public Set<Documento> listarDocumentos(@PathVariable("idUsuario") Long idUsuario) {
		return repositorio.findById(idUsuario).get().getDocumentos();
	}
	
	@PostMapping("/{idUsuario}/adicionarDocumentos")
	public Set<Documento> adicionarDocumentos(@PathVariable("idUsuario") Long idUsuario, @RequestBody List<Documento> documentos) {
		Usuario usuario = repositorio.findById(idUsuario).get();
		documentos.forEach(doc -> usuario.getDocumentos().add(doc));
		return repositorio.save(usuario).getDocumentos();
	}
	
	@PutMapping("/{idUsuario}/atualizarDocumentos")
	public Set<Documento> atualizarDocumentos(@PathVariable("idUsuario") Long idUsuario, @RequestBody Set<Documento> documentos) {
		Usuario usuario = repositorio.findById(idUsuario).get();
		documentoAtualizador.atualizar(usuario.getDocumentos(), documentos);
		return repositorio.save(usuario).getDocumentos();
	}
	
	@DeleteMapping("/{idUsuario}/excluirDocumentos")
	public Set<Documento> excluirDocumentos(@PathVariable("idUsuario") Long idUsuario, @RequestBody List<Long> idsDocumentos) {
		Usuario usuario = repositorio.findById(idUsuario).get();
		usuario.setDocumentos(usuario.getDocumentos().stream().filter(doc -> !idsDocumentos.contains(doc.getId())).collect(Collectors.toSet()));
		return repositorio.save(usuario).getDocumentos();
	}
}
