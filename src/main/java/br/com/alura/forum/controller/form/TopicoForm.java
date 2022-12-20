package br.com.alura.forum.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.NonNull;

import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import net.bytebuddy.utility.nullability.NeverNull;

//DTO para receber dados de entrada.
//POJO - Java bean.
public class TopicoForm {
	
//	@Autowired
//	private CursoRepository topicoRepository;
//	
//  Tópicos do bean validation evitam os if´s na classe controller. 
//  Tem que colocar o @Valid no controller para que o spring use o bean validation. 
	
	@NotNull  @Size(min=5) @NotEmpty
	private String titulo;
	@NotNull  @Size(min=10) @NotEmpty
	private String mensagem;
	@NotNull @NotEmpty
	private String nomeCurso;
	
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
	public String getNomeCurso() {
		return nomeCurso;
	}
	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}
	public Topico converter(CursoRepository cursoRepository) {
		Curso curso = cursoRepository.findByNome(nomeCurso);
		return new Topico(titulo, mensagem , curso);
	}
	
	

}
