package com.ramon.gerenciadorfinancas.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ramon.gerenciadorfinancas.exception.RegraNegocioException;
import com.ramon.gerenciadorfinancas.model.Usuario;
import com.ramon.gerenciadorfinancas.repository.UsuarioRepository;
import com.ramon.gerenciadorfinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	private UsuarioService usuarioService;
	
	
	private UsuarioRepository repository;
	
	@Before
	public void setUp() {
		repository = Mockito.mock(UsuarioRepository.class);
		usuarioService = new UsuarioServiceImpl(repository);
	}
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
	 	
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
	 	usuarioService.validarEmail("emai@email.com");
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveRetornarErroAoValidadeEmailQuandoExisterEmailCadastrado() {
	
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		
	 	usuarioService.validarEmail("usuario@email.com");
		
	}
}
