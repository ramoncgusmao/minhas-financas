package com.ramon.gerenciador.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon.gerenciadorfinancas.model.Lancamento;


public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{

}
