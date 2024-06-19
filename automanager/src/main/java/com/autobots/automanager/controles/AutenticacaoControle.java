package com.autobots.automanager.controles;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.dto.AutenticacaoDto;

@RestController
public class AutenticacaoControle {
	
	private final AuthenticationManager authenticationManager;
	
	public AutenticacaoControle(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@PostMapping("/login")
	public void login(@RequestBody AutenticacaoDto dto) {
		this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getNomeUsuario(), dto.getSenha()));
	}
}
