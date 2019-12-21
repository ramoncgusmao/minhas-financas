package com.ramon.gerenciadorfinancas.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ramon.gerenciadorfinancas.exception.ErroAutenticacao;
import com.ramon.gerenciadorfinancas.exception.RegraNegocioException;
import com.ramon.gerenciadorfinancas.model.Lancamento;
import com.ramon.gerenciadorfinancas.model.Usuario;
import com.ramon.gerenciadorfinancas.model.enuns.StatusLancamento;
import com.ramon.gerenciadorfinancas.model.enuns.TipoLancamento;
import com.ramon.gerenciadorfinancas.repository.LancamentoRepository;
import com.ramon.gerenciadorfinancas.repository.LancamentoRepositoryTest;
import com.ramon.gerenciadorfinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento(){
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
	}
	
	public void naoDeveSalvarUmLancamento() {
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		
		Assertions.catchThrowableOfType(() -> service.salvar(lancamentoASalvar),RegraNegocioException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
		
	}
	
	@Test
	public void deveAtualizarUmLancamento(){
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		Lancamento lancamento = service.atualizar(lancamentoSalvo);
		
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
	
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo(){
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		
	
		Assertions.catchThrowableOfType(() -> service.atualizar(lancamentoASalvar), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
	
	}
	
	@Test
	public void deveDeletarUmLancamento(){
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);

		service.deletar(lancamento);
		
		
		Mockito.verify(repository).delete(lancamento);
	
	}
	
	
	@Test
	public void naoDeveDeletarUmLancamento(){
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		

		Assertions.catchThrowableOfType(() -> service.deletar(lancamento), NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
	
	}
	
	
	@Test
	public void deveFiltrarLancamento(){
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		List<Lancamento> resultado = service.buscar(lancamento);
		
		Assertions
			.assertThat(resultado)
			.isNotEmpty()
			.hasSize(1)
			.contains(lancamento);
	
	}
	
	@Test
	public void deveAtualizarUmStatusDeUmLancamento() {
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
	
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		service.atualizarStatus(lancamento, novoStatus);
		
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
		
	}
	
	@Test
	public void deveObterUmLancamentoPorId() {
		 Long id = 1l;
		 
		 Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		 lancamento.setId(id);
		 
		 Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		 
		 Optional<Lancamento> resultado = service.obterPorId(id);
		 Assertions.assertThat(resultado.isPresent()).isTrue();
		 
		
	}
	
	@Test
	public void naoDeveRetornarUmLancamentoPorId() {
		 Long id = 1l;
		 
		 
		 Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		 
		 Optional<Lancamento> resultado = service.obterPorId(id);
		 Assertions.assertThat(resultado.isPresent()).isFalse();
		
	}
	
	@Test
	public void validarOMetodoValidar() {
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setDescricao(null);
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		lancamento.setUsuario(usuario);
		
		testeValidacaoDoValidar(lancamento, "Informe uma descrição válida.");
		
		lancamento.setDescricao("");
		
		testeValidacaoDoValidar(lancamento, "Informe uma descrição válida.");
		
		lancamento.setDescricao("descricao");
		lancamento.setMes(null);
		
		testeValidacaoDoValidar(lancamento, "Informe um mês válido");
		lancamento.setMes(15);
		
		
		testeValidacaoDoValidar(lancamento, "Informe um mês válido");
		
		lancamento.setMes(-2);

		testeValidacaoDoValidar(lancamento, "Informe um mês válido");
		lancamento.setMes(1);
		
		lancamento.setUsuario(null);

		testeValidacaoDoValidar(lancamento, "Informe um usuario válido");
		
		lancamento.setUsuario(new Usuario());

		testeValidacaoDoValidar(lancamento, "Informe um usuario válido");
		
		lancamento.setUsuario(usuario);
		lancamento.setValor(null);

		testeValidacaoDoValidar(lancamento, "Informe um valor válido");
		
		lancamento.setValor(BigDecimal.valueOf(-2));

		testeValidacaoDoValidar(lancamento, "Informe um valor válido");
		
		lancamento.setValor(BigDecimal.valueOf(3));

		lancamento.setTipo(null);
		testeValidacaoDoValidar(lancamento, "Informe um tipo de lançamento");
		
	}

	private void testeValidacaoDoValidar(Lancamento lancamento, String mensagem) {
		Throwable exception = Assertions.catchThrowable(() -> service.validar(lancamento));
		Assertions.assertThat(exception)
		.isInstanceOf(RegraNegocioException.class)
		.hasMessage(mensagem);
	}
}
