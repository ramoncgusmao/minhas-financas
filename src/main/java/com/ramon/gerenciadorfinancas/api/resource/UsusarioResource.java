package com.ramon.gerenciadorfinancas.api.resource;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ramon.gerenciadorfinancas.api.dto.UsuarioDTO;
import com.ramon.gerenciadorfinancas.exception.ErroAutenticacao;
import com.ramon.gerenciadorfinancas.exception.RegraNegocioException;
import com.ramon.gerenciadorfinancas.model.Usuario;
import com.ramon.gerenciadorfinancas.service.LancamentoService;
import com.ramon.gerenciadorfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsusarioResource {

	@Autowired
	private UsuarioService service;
	
	@Autowired
	private LancamentoService lancamentoService;

	@PostMapping()
	public ResponseEntity salvar(@RequestBody UsuarioDTO usuarioDTO) {

		Usuario usuario = usuarioToDto(usuarioDTO);
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.CREATED);
		}catch(RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}

	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO usuarioDTO) {
		
		try {
			Usuario usuarioAutenticado = service.autentica(usuarioDTO.getEmail(), usuarioDTO.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		}catch(ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") Long id) {
		 Optional<Usuario> usuario =  service.obterPorId(id);
		 if(!usuario.isPresent()) {
			 return new ResponseEntity("Usuario não encontrado",HttpStatus.NOT_FOUND);
		 }
		BigDecimal resultado = lancamentoService.obterSaldoPorUsuario(id);
		
		return ResponseEntity.ok(resultado);
	}
	
	private Usuario usuarioToDto(UsuarioDTO usuarioDTO) {
		return Usuario.builder()
				.email(usuarioDTO.getEmail())
				.senha(usuarioDTO.getSenha())
				.nome(usuarioDTO.getNome())
				.build();
	}
}
