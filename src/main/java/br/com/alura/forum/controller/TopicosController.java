package br.com.alura.forum.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.modelo.Curso;
import br.com.alura.forum.modelo.Topico;

//Biblioteca jackson faz a conversão de java para json.
//@Controller
//CTRL + 1 = criar classe.
@RestController //O spring assume que tem um @ResponseBody para cada método.
public class TopicosController {
	
	@RequestMapping("/topicos")
	//@ResponseBody Tem que colocar o @ResponseBody  para que o spring entenda que
	//não precisa direcionar para outra página. Por ser uma api rest.
	public List<TopicoDto> lista(){ //Ao devolver a classe de domínio (JPA), sempre devolve todos os atributos.
		                         //Por isso não é uma boa prática!
		
		Topico topico = new Topico("Duvida", "Duvida com spring" , new Curso("Spring", "Programação"));
		
		//Devolve um array.
		//return Arrays.asList(topico, topico, topico);
		//Com o dto há uma flexibilização da quantidade dos valores devolvidos.
		return TopicoDto.converter(Arrays.asList(topico, topico, topico));
		
	}

}
