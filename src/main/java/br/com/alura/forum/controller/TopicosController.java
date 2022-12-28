package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
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
	//No método lista o parâmetro não vem no corpo da solicitação como no método post.
	//Mas vem na url, por isso é necessário colocar o requestparam.
	//Com o requestparam o spring entende como obrigatório o parâmetro.
	//@EnableSpringDataWebSupport - Permite passar para o spring data os parâmetros de ordenação e paginação.
	//Antes de @EnableSpringDataWebSupport  localhost:8081/topicos?pagina=0&qtd=3&ordenacao=titulo
	//localhost:8081/topicos?page=0&size=10&sort=id,desc&sort=dataCriacao,desc
	//Só usa o parâmetro de ordenação quando não tem o pageable default.
	@GetMapping
	public Page<TopicoDto> lista(@RequestParam(required = false ) String nomeCurso,
			@PageableDefault(sort= "id" , direction= Direction.DESC, page = 0, size = 10) Pageable paginacao ){ //Ao devolver a classe de domínio (JPA), sempre devolve todos os atributos.
		                         //Por isso não é uma boa prática!
		
		//Pageable paginacao = PageRequest.of(pagina, qtd, Direction.ASC, ordenacao);
		
		//Topico topico = new Topico("Duvida", "Duvida com spring" , new Curso("Spring", "Programação"));
		
		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			
			//Devolve um array.
			//return Arrays.asList(topico, topico, topico);
			//Com o dto há uma flexibilização da quantidade dos valores devolvidos.
			return TopicoDto.converter(topicos);
		} else {
			//Para criar o método, no repositório, usam-se as teclas ctrl + 1.
			//findByCursoNome = nome do relacionamento concatenado pelo nome do atributo
			//da entidade do relacionamento.
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			
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
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder ) {	
		
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		//Devolve um cabeçalho HTTP com a url do novo recurso criado.
		//E a segunda coisa é a devolução do recurso que acabou de criar.
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
		
	}
	
	@GetMapping("/{id}")
	//@PathVariable diz que a variável é da url.
	//O spring associa os dois parâmetros.
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) { 
	//public TopicoDto detalhar(@PathVariable("id") Long codigo) { pode ser assim tb.
		//Topico topico = topicoRepository.getReferenceById(id);
		//Outra forma de tratar erros.
		Optional<Topico> topico = topicoRepository.findById(id);
		if (topico.isPresent()) {
		  return  ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));	
		}
		
		return ResponseEntity.notFound().build();
		
		
	}
	
	//Tem dois métodos para atualizar: o put e o patch
	//O put serve para sobrescrever o recurso inteiro.
	//O patch faz uma pequena atualização, muda apenas alguns campos.
	//Quando carrega pelo id já está sendo gerenciado pela jpa.
	//Ao final do método a JPA faz o update e commit automaticamente.
	@PutMapping("/{id}")
	@Transactional //Dispara o commit no BD.
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id,@RequestBody @Valid AtualizacaoTopicoForm form) {
		
		Optional<Topico> optional = topicoRepository.findById(id);
		if (optional.isPresent()) {
			//Quando chega nesta linha já atualizou em memória.
			//Quando terminar o método atualiza no BD.
			Topico topico = form.atualizar(id, topicoRepository );
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		
		return ResponseEntity.notFound().build();
		
		
		
		
		
		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> optional = topicoRepository.findById(id);
		
		if (optional.isPresent()) {
			//Quando chega nesta linha já atualizou em memória.
			//Quando terminar o método atualiza no BD.
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
		
		
		
		
	}

}
