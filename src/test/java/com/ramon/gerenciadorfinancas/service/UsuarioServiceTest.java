package com.ramon.gerenciadorfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ramon.gerenciadorfinancas.exception.ErroAutenticacao;
import com.ramon.gerenciadorfinancas.exception.RegraNegocioException;
import com.ramon.gerenciadorfinancas.model.Usuario;
import com.ramon.gerenciadorfinancas.repository.UsuarioRepository;
import com.ramon.gerenciadorfinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	@SpyBean
	private UsuarioServiceImpl usuarioService;
	
	@MockBean
	private UsuarioRepository repository;

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
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUsuarioComSucesso() {
		
		String email = "email@email.com";
		String senha = "senha";

		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		Usuario result = usuarioService.autentica(email, senha);
		
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroAutenticacaoQuandoNaoEncontrarUsuarioComEmailInformado() {
		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		
		Throwable exception = Assertions.catchThrowable( () -> usuarioService.autentica("email@email.com", "senha"));
		Assertions.assertThat(exception)
					.isInstanceOf(ErroAutenticacao.class)
					.hasMessage("Usuario não encontrado para o email informado");
		
	}
	
	
	@Test
	public void deveLancarErroAutenticacaoQuandoASenhaEstiverDiferenteInformado() {
		String email = "email@email.com";
		String senha = "senha";

		Usuario usuario = Usuario.builder().email(email).senha(senha).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		Throwable exception = Assertions.catchThrowable( () -> usuarioService.autentica(email, "oake"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha invalida");
		
	}
	
	
	@Test
	public void deveSalvarUmUsuario() {
		Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());
		
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("nome")
				.email("email@email.com")
				.senha("senha")
				.build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
		
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(usuario.getId());
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo(usuario.getNome());
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo(usuario.getEmail());
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo(usuario.getSenha());
		
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		String email = "email@email.com";
		Usuario usuario = Usuario.builder()
				.email(email).build();
		
		Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);
		
		usuarioService.salvarUsuario(usuario);
		
		Mockito.verify(repository, Mockito.never()).save(usuario);
	}
}
