package com.ramon.gerenciadorfinancas.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.transaction.annotation.Transactional;

import com.ramon.gerenciadorfinancas.model.Lancamento;
import com.ramon.gerenciadorfinancas.model.enuns.StatusLancamento;
import com.ramon.gerenciadorfinancas.repository.LancamentoRepository;
import com.ramon.gerenciadorfinancas.service.LancamentoService;

public class LancamentoServiceImpl implements LancamentoService{

	private LancamentoRepository repository;
	
	 public LancamentoServiceImpl(LancamentoRepository repository) {
		this.repository = repository;
		
	}
	@Override
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		// TODO Auto-generated method stub
		return repository.save(lancamento);
	}

	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		return  repository.save(lancamento);
	}

	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {
		Example example = Example.of(lancamentoFiltro, ExampleMatcher
				.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);
	}

	@Override
	@Transactional
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
	}

}
