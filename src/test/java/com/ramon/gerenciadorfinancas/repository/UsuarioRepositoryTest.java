package com.ramon.gerenciadorfinancas.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ramon.gerenciador.repository.UsuarioRepository;
import com.ramon.gerenciadorfinancas.model.Usuario;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").build();
		
		repository.save(usuario);
		
		Assertions.assertThat(repository.existsByEmail("usuario@email.com")).isTrue();
	}
}
