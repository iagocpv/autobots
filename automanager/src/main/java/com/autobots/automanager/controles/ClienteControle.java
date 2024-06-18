package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
@RequestMapping("/clientes")
public class ClienteControle {
	@Autowired
	private ClienteRepositorio repositorio;
	@Autowired
	private TelefoneAtualizador telefoneAtualizador;
	@Autowired
	private DocumentoAtualizador documentoAtualizador;

	@GetMapping("/{idCliente}")
	public Cliente obterCliente(@PathVariable("idCliente") long idCliente) {
		return repositorio.findById(idCliente).get();
	}

	@GetMapping
	public List<Cliente> obterClientes() {
		return repositorio.findAll();
	}
	
	/* formato json
	{
		"nome": "Nome 2",
		"nomeSocial": "Nome Social 2",
		"dataNascimento": "2024-06-05",
		"dataCadastro": "2024-06-05",
		"endereco": {
			"cidade": "sjc",
			"estado": "sp",
			"bairro": "sei la",
			"rua": "sei la",
			"numero": "30",
			"codigoPostal": "124567",
			"informacoesAdicionais": "info"
		},
		"telefones": [
			{
				"ddd": "12",
				"numero": "987654124"
			}
		],
		"documentos": [
			{
				"tipo": "RG",
				"numero": "41235738217"
			},
			{
				"tipo": "CPF",
				"numero": "278963487946"
			}
		]
	} 
	*/

	@PostMapping("/cadastrar")
	public Cliente cadastrarCliente(@RequestBody Cliente cliente) {
		return repositorio.save(cliente);
	}

	@PutMapping("/atualizar")
	public Cliente atualizarCliente(@RequestBody Cliente atualizacao) {
		Cliente cliente = repositorio.getById(atualizacao.getId());
		ClienteAtualizador atualizador = new ClienteAtualizador();
		atualizador.atualizar(cliente, atualizacao);
		return repositorio.save(cliente);
	}

	@DeleteMapping("/{idCliente}")
	public void excluirCliente(@PathVariable("idCliente") long idCliente) {
		repositorio.deleteById(idCliente);
	}
	
	@PostMapping("/{idCliente}/adicionarTelefones")
	public List<Telefone> adicionarTelefones(@PathVariable("idCliente") Long idCliente, @RequestBody List<Telefone> telefones) {
		Cliente cliente = repositorio.getById(idCliente);
		telefones.forEach(tel -> cliente.getTelefones().add(tel));
		return repositorio.save(cliente).getTelefones();
	}
	
	@GetMapping("/{idCliente}/listarTelefones")
	public List<Telefone> listarTelefones(@PathVariable("idCliente") Long idCliente) {
		return repositorio.getById(idCliente).getTelefones();
	}
	
	@PutMapping("/{idCliente}/atualizarTelefones")
	public List<Telefone> atualizarTelefones(@PathVariable("idCliente") Long idCliente, @RequestBody List<Telefone> telefones) {
		Cliente cliente = repositorio.getById(idCliente);
		telefoneAtualizador.atualizar(cliente.getTelefones(), telefones);
		return repositorio.save(cliente).getTelefones();
	}
	
	@DeleteMapping("/{idCliente}/excluirTelefones")
	public List<Telefone> excluirTelefones(@PathVariable("idCliente") Long idCliente, @RequestBody List<Long> idsTelefones) {
		Cliente cliente = repositorio.getById(idCliente);
		cliente.setTelefones(cliente.getTelefones().stream().filter(tel -> !idsTelefones.contains(tel.getId())).collect(Collectors.toList()));
		return repositorio.save(cliente).getTelefones();
	}
	
	@GetMapping("/{idCliente}/listarDocumentos")
	public List<Documento> listarDocumentos(@PathVariable("idCliente") Long idCliente) {
		return repositorio.getById(idCliente).getDocumentos();
	}
	
	@PostMapping("/{idCliente}/adicionarDocumentos")
	public List<Documento> adicionarDocumentos(@PathVariable("idCliente") Long idCliente, @RequestBody List<Documento> documentos) {
		Cliente cliente = repositorio.getById(idCliente);
		documentos.forEach(doc -> cliente.getDocumentos().add(doc));
		return repositorio.save(cliente).getDocumentos();
	}
	
	@PutMapping("/{idCliente}/atualizarDocumentos")
	public List<Documento> atualizarDocumentos(@PathVariable("idCliente") Long idCliente, @RequestBody List<Documento> documentos) {
		Cliente cliente = repositorio.getById(idCliente);
		documentoAtualizador.atualizar(cliente.getDocumentos(), documentos);
		return repositorio.save(cliente).getDocumentos();
	}
	
	@DeleteMapping("/{idCliente}/excluirDocumentos")
	public List<Documento> excluirDocumentos(@PathVariable("idCliente") Long idCliente, @RequestBody List<Long> idsDocumentos) {
		Cliente cliente = repositorio.getById(idCliente);
		cliente.setDocumentos(cliente.getDocumentos().stream().filter(doc -> !idsDocumentos.contains(doc.getId())).collect(Collectors.toList()));
		return repositorio.save(cliente).getDocumentos();
	}
}
