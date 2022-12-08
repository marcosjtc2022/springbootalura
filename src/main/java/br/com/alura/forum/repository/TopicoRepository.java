package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.forum.modelo.Topico;

//Passa a entidade e o tipo de attibuto da chave primária desta entidade.
public interface TopicoRepository extends JpaRepository<Topico,Long> {

	//Curso é o nome da entidade de relacionamento e curso é o atributo dentro,
	//desta entidade de relacionamento.
	List<Topico> findByCursoNome(String nomeCurso);
	
	
	//Pode buscar em todos os relacionamentos
	//List<Topico> findByCategoriaNome(String nomeCurso);
	
	//Caso haja um atributo dentro de topico com nome cursoNome,
	//é necessário colocar um traço(underscore) na pesquisa,
	//para diferenciar.
	//List<Topico> findByCurso_Nome(String nomeCurso);
	
	//Pode fazer usando a notação @Query
	//@Query("Select t from topico t where t.curso.nome = : nomeCurso")
	//List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso")String nomeCurso);

}
