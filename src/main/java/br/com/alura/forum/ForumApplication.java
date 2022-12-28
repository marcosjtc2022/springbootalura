package br.com.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
//Permite passar para o spring data os parâmetros de ordenação e paginação.
@EnableSpringDataWebSupport 
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
