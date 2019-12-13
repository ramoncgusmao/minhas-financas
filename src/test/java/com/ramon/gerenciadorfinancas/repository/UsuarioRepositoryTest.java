package com.ramon.gerenciadorfinancas.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ramon.gerenciadorfinancas.model.Usuario;
import com.ramon.gerenciadorfinancas.repository.UsuarioRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		
		Usuario usuario = criarUsuario();
		
		entityManager.persist(usuario);
		
		Assertions.assertThat(repository.existsByEmail("usuario@email.com")).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComEmail() {
		
		Assertions.assertThat(repository.existsByEmail("usuario@email.com")).isFalse();
		
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		
		Usuario usuario = criarUsuario();
		
		Usuario usuarioSave = repository.save(usuario);
		
		Assertions.assertThat(usuarioSave.getId()).isNotNull();
	}

	
	private static Usuario criarUsuario() {
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@email.com").senha("senha").build();
		return usuario;
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		
		Usuario usuario = criarUsuario();
		
		entityManager.persist(usuario);
		
		Optional<Usuario> usuarioFind = repository.findByEmail(usuario.getEmail()); 
		Assertions.assertThat(usuarioFind.isPresent()).isTrue();
	}
	
	@Test
	public void deveRetornarVazioBuscarUsuariPorEmail() {
		
		Usuario usuario = criarUsuario();
		
		Optional<Usuario> usuarioFind = repository.findByEmail(usuario.getEmail()); 
		Assertions.assertThat(usuarioFind.isPresent()).isFalse();
	}
}
