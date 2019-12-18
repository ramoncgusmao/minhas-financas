package com.ramon.gerenciadorfinancas.service;

import java.util.List;

import com.ramon.gerenciadorfinancas.model.Lancamento;
import com.ramon.gerenciadorfinancas.model.enuns.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
}
