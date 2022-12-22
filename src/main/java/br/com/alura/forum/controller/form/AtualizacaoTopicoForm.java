package br.com.alura.forum.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;

public class AtualizacaoTopicoForm {
	
	@NotNull  @Size(min=5) @NotEmpty
	private String titulo;
	@NotNull  @Size(min=10) @NotEmpty
	private String mensagem;
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public Topico atualizar(Long id, TopicoRepository topicoRepository) {
		//Quando carrega pelo id já está sendo gerenciado pela jpa.
		Topico topico = topicoRepository.getReferenceById(id);
		topico.setTitulo(this.titulo);
		topico.setMensagem(this.mensagem);
		return topico;
	}
	
	
	

}
