package br.com.alura.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HelloController {
	
	@RequestMapping("/")
	@ResponseBody //Evita que o spring procure uma página.
	//Devolve string diretamente para o navegador.
	public String hello() {
		return "Hello World";
	}

}
