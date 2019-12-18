package com.ramon.gerenciadorfinancas.service;

import java.util.Optional;

import com.ramon.gerenciadorfinancas.model.Usuario;

public interface UsuarioService {

	Usuario autentica(String email, String senha);
	
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);

	Optional<Usuario> obterPorId(Long id);
}
