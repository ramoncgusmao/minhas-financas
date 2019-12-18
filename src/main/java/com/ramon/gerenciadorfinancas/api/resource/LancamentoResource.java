package com.ramon.gerenciadorfinancas.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ramon.gerenciadorfinancas.api.dto.LancamentoDTO;
import com.ramon.gerenciadorfinancas.exception.RegraNegocioException;
import com.ramon.gerenciadorfinancas.model.Lancamento;
import com.ramon.gerenciadorfinancas.model.Usuario;
import com.ramon.gerenciadorfinancas.model.enuns.StatusLancamento;
import com.ramon.gerenciadorfinancas.model.enuns.TipoLancamento;
import com.ramon.gerenciadorfinancas.service.LancamentoService;
import com.ramon.gerenciadorfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamento")
public class LancamentoResource {

	@Autowired
	private LancamentoService service;

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping
	private ResponseEntity salvar(@RequestBody LancamentoDTO dto) {

		try {

			Lancamento entidade = converter(dto);
			entidade = service.salvar(entidade);
			return ResponseEntity.ok(entidade);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping
	private ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {

		return service.obterPorId(id).map(entity -> {
			try {
				Lancamento entidade = converter(dto);
				entidade.setId(entity.getId());
				entidade = service.atualizar(entidade);
				return ResponseEntity.ok(entidade);

			} catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));

	}

	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id)
				.map(entidade -> {
					service.deletar(entidade);
					return new ResponseEntity(HttpStatus.NO_CONTENT);
				}).orElseGet(() -> new ResponseEntity("Lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST) );
		
	}
	private Lancamento converter(LancamentoDTO dto) {
		Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("usuario nao encontrado"));

		return Lancamento.builder().descricao(dto.getDescricao()).ano(dto.getAno()).mes(dto.getMes())
				.valor(dto.getValor()).usuario(usuario).tipo(TipoLancamento.valueOf(dto.getTipo()))
				.status(StatusLancamento.valueOf(dto.getStatus())).build();
	}
}
