package com.ramon.gerenciadorfinancas.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ramon.gerenciadorfinancas.model.Lancamento;
import com.ramon.gerenciadorfinancas.model.enuns.StatusLancamento;
import com.ramon.gerenciadorfinancas.model.enuns.TipoLancamento;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

	@Autowired
	private LancamentoRepository repository;

	@Autowired
	TestEntityManager entityManager;

	@Test
	public void deveSalvarUmLancamento() {
		Lancamento lancamento = criarLancamento();

		lancamento = repository.save(lancamento);

		assertThat(lancamento.getId()).isNotNull();

	}

	@Test
	public void deveDeletarUmLancamento() {
		Lancamento lancamento = criarEpersistirLancamento();

		Lancamento lancamentoNovo = entityManager.find(Lancamento.class, lancamento.getId());

		repository.delete(lancamento);

		Lancamento lancamentoDeletado = entityManager.find(Lancamento.class, lancamento.getId());
		assertThat(lancamentoDeletado).isNull();
	}

	private Lancamento criarEpersistirLancamento() {
		Lancamento lancamento = criarLancamento();
		lancamento = entityManager.persist(lancamento);
		return lancamento;
	}

	@Test
	public void deveAtualizarUmLancamento() {
		Lancamento lancamento = criarEpersistirLancamento();

		lancamento.setAno(2018);
		lancamento.setDescricao("teste atualizar");
		lancamento.setStatus(StatusLancamento.CANCELADO);

		repository.save(lancamento);
		Lancamento lancAtualizado = entityManager.find(Lancamento.class, lancamento.getId());

		assertThat(lancAtualizado.getAno()).isEqualTo(2018);
		assertThat(lancAtualizado.getDescricao()).isEqualTo("teste atualizar");
		assertThat(lancAtualizado.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
	}

	@Test
	public void deveBuscarLancamentoPeloId() {
		Lancamento lancamento = criarEpersistirLancamento();

		Optional<Lancamento> lancamentoEncontrado = repository.findById(lancamento.getId());

		assertThat(lancamentoEncontrado.isPresent()).isTrue();
	}

	public static Lancamento criarLancamento() {
		Lancamento lancamento = Lancamento.builder().ano(2019).mes(1).descricao("lancamento qualquer")
				.valor(BigDecimal.valueOf(10)).tipo(TipoLancamento.RECEITA).status(StatusLancamento.PENDENTE)
				.dataCadastro(LocalDate.now()).build();
		return lancamento;
	}
}
