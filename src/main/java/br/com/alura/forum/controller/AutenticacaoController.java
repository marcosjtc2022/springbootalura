package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controller.dto.TokenDto;
import br.com.alura.forum.controller.form.LoginForm;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
	
	//Spring não consegue injetar automaticamente. Teve que colocar no SecurityConfiguration.
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form){
		
		UsernamePasswordAuthenticationToken dados = form.converter();
		
		try {
			
			Authentication authentication = authenticationManager.authenticate(dados);
			String token = tokenService.gerarToken(authentication);
			//bearer manda o token junto
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
//			System.out.println(form.getSenha());
			
			
		} catch (AuthenticationException e) {
			
			return ResponseEntity.badRequest().build();
			
		}
		
	}

}
