package br.com.alura.forum.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import br.com.alura.forum.modelo.Topico;

public class TopicoDto {
	
	//Colocar tipos primitivos do java, e não classes de domínio.
	
	private Long id;
	private String titulo;
	private String mensagem;
	private LocalDateTime dataCriacao;
	
	public TopicoDto(Topico topico) {
		this.id = topico.getId();
		this.titulo = topico.getTitulo();
		this.mensagem = topico.getMensagem();
		this.dataCriacao = topico.getDataCriacao();
	}
	
	public Long getId() {
		return id;
	}
	public String getTitulo() {
		return titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}
	
	
	public static List<TopicoDto> converter(List<Topico> topicos){
		//Api de stream do java 8 para não precisar fazer um loop manualmente.
		//Map de topico para topicodto
		//TopicoDto::new chama o construtor que recebe o topico como parâmetro.
		//.collect(Collectors.toList()) transforma em uma lista.
		return topicos.stream().map(TopicoDto::new).collect(Collectors.toList());
	}
	
	
	

}
