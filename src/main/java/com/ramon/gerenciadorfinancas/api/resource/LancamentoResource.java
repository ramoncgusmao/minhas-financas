package com.ramon.gerenciadorfinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ramon.gerenciadorfinancas.api.dto.LancamentoDTO;
import com.ramon.gerenciadorfinancas.api.dto.LancamentoStatusDTO;
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
			return new ResponseEntity(entidade, HttpStatus.CREATED);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
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
	
	@GetMapping
	public ResponseEntity buscar( @RequestParam(value = "descricao", required = false) String descricao, 
								 @RequestParam(value = "mes", required = false) Integer mes,
								 @RequestParam(value = "ano", required = false) Integer ano,
								 @RequestParam("usuario") Long idUsuario) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
		Optional<Usuario> usuario = usuarioService.obterPorId(idUsuario);
		
		if(!usuario.isPresent()) {
			return ResponseEntity.badRequest().body("Não foi possivel realizar a consulta. Usuario não encontrado para o Id informado.");
		}else {
			lancamentoFiltro.setUsuario(usuario.get());
		}
		
		List<Lancamento> lancamentos =  service.buscar(lancamentoFiltro);
		return ResponseEntity.ok().body(lancamentos);
		}
	
	@PutMapping("{id}/atualizar-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody LancamentoStatusDTO atualizar) {
		return service.obterPorId(id).map( entity -> {
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(atualizar.getStatus());
			if(statusSelecionado == null) {
				return ResponseEntity.badRequest().body("não foi possivel atualizar o status do lancamento, envie um status valido");
			}
			try {
				entity.setStatus(statusSelecionado);
				service.atualizar(entity);
				return ResponseEntity.ok().body(entity);
			}catch (Exception e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}).orElseGet( () ->
				new ResponseEntity("lancamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));

				
				
	}
	
	
	private Lancamento converter(LancamentoDTO dto) {
		Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("usuario nao encontrado"));

		
		Lancamento lancamento =  Lancamento.builder().descricao(dto.getDescricao()).ano(dto.getAno()).mes(dto.getMes())
				.valor(dto.getValor()).usuario(usuario).tipo(TipoLancamento.valueOf(dto.getTipo()))
				.build();
		
		if(dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		return lancamento;
	}
}
