package com.ramon.gerenciadorfinancas.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioDTO {

	private String email;
	private String senha;
	private String nome;
}
