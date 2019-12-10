package com.ramon.gerenciadorfinancas.service.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ramon.gerenciador.repository.UsuarioRepository;
import com.ramon.gerenciadorfinancas.exception.RegraNegocioException;
import com.ramon.gerenciadorfinancas.model.Usuario;
import com.ramon.gerenciadorfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	
	@Override
	public Usuario autentica(String email, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validarEmail(String email) {
		
		if(true) {
			throw new RegraNegocioException("Já existe um usuario cadastrado com este email.");
		}
	}
	
}
