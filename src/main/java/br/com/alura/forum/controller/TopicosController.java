package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

//Biblioteca jackson faz a conversão de java para json.
//@Controller
//CTRL + 1 = criar classe.
@RestController //O spring assume que tem um @ResponseBody para cada método.
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	//A tag @RequestMapping(value="/topicos", method = RequestMethod.GET),
	//permite colocar o mesmo nome da URI, mas com method = RequestMethod diferente.
	//URI iguais porém com métodos diferentes.
	//@RequestMapping(value="/topicos", method = RequestMethod.GET)
	//@ResponseBody Tem que colocar o @ResponseBody  para que o spring entenda que
	//não precisa direcionar para outra página. Por ser uma api rest.
	//http://localhost:8081/topicos?nomeCurso=Spring
	//http://localhost:8081/topicos?nomeCurso=Spring+Boot (Filtra por parâmetro)
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso){ //Ao devolver a classe de domínio (JPA), sempre devolve todos os atributos.
		                         //Por isso não é uma boa prática!
		
		//Topico topico = new Topico("Duvida", "Duvida com spring" , new Curso("Spring", "Programação"));
		
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			
			//Devolve um array.
			//return Arrays.asList(topico, topico, topico);
			//Com o dto há uma flexibilização da quantidade dos valores devolvidos.
			return TopicoDto.converter(topicos);
		} else {
			//Para criar o método, no repositório, usam-se as teclas ctrl + 1.
			//findByCursoNome = nome do relacionamento concatenado pelo nome do atributo
			//da entidade do relacionamento.
            List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			
			//Devolve um array.
			//return Arrays.asList(topico, topico, topico);
			//Com o dto há uma flexibilização da quantidade dos valores devolvidos.
			return TopicoDto.converter(topicos);
			
		}
		
	}
	
	//@RequestMapping(value="/topicos", method = RequestMethod.POST)
	//@RequestBody informa para o spring que as informações vêm no corpo
	//da requisição.
	@PostMapping
	//Diferenciar o DTO de quando manda do DTO de quando recebe.
	//200 é o código de retorno OK genérico.
	//O ideal é que devolva o código 201 que significa que foi
	//devolvido com sucesso e um novo recurso foi criado no
	//servidor. Por isso é importante evitar o void
	//public void cadastrar(@RequestBody TopicoForm form) {
	//@Valid executa as anotações do bean validation.
	//Se houver algo errado nem entra no método e devolve o código 400.
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder ) {	
		
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		//Devolve um cabeçalho HTTP com a url do novo recurso criado.
		//E a segunda coisa é a devolução do recurso que acabou de criar.
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
		
	}

}
