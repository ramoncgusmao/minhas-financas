package com.ramon.gerenciadorfinancas.service.impl;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon.gerenciadorfinancas.exception.ErroAutenticacao;
import com.ramon.gerenciadorfinancas.exception.RegraNegocioException;
import com.ramon.gerenciadorfinancas.model.Usuario;
import com.ramon.gerenciadorfinancas.repository.UsuarioRepository;
import com.ramon.gerenciadorfinancas.service.UsuarioService;


@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		// TODO Auto-generated constructor stub
		this.repository = repository;
	}
	
	@Override
	public Usuario autentica(String email, String senha) {
		
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuario não encontrado para o email informado");
		}
		
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha invalida");
		}
		
		
		return usuario.get();
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		
		if(repository.existsByEmail(email)) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com este email.");
		}
	}
	
}
