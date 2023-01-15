package br.com.alura.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

//OncePerRequestFilter filtro do spring chamado um única vez a cada requisição.
public class AutenticacaoViaTokenFilter extends OncePerRequestFilter{
	
	//Nesse tipo de classe não se consegue fazer injeção de dependências.
	//Por isso tem que gerar o construtor.
	private TokenService tokenService;
	
	
    //Como não pode injetar via tag @Autowired, injeta via construtor.
	//Quando fizer o new, tem que passar o tokenService como parâmetro.
	public AutenticacaoViaTokenFilter(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Pega o token no cabeçalho , autentica, se estiver correto autentica.
		//Como a requisição é stateless não existe mais a concepção de usuário logado.
		//Quando o token é gerado não é guardado do lado do servidor.
		//A API não sabe quem está logado.
		//A autenticação é feita para cada requisição.
		//Pega o token, autentica e executa a lógica que foi requisitada.
		
		String token = recuperarToken(request);
		boolean valido = tokenService.isTokenValido(token);
		
		//Spring detecta a partir do retorno de tokenService.isTokenValido(token); se está autorizado.
		//Se não estiver não segue o fluxo e bloqueia a requisição.
		filterChain.doFilter(request, response);
		
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		
		//Se tem o cabeçalho Authorization mas está vazio.
		if ((token == null)||(token.isEmpty()||(!token.startsWith("Bearer ")) )) {
			return null;
		}
		
		//7 = Bearer 
		return token.substring(7, token.length());
	}

}
