package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	//Injeta variável do application properties.
	@Value("@{forum.jwt.expiration}")
	private String expiration;
	
	//Injeta variável do application properties.
	@Value("@{forum.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authentication) {
		
		Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		//Date dataExpiracao = new Date(hoje.getTime());
		
		
		return Jwts.builder()
				.setIssuer("API do fórum Alura")
				.setSubject(usuarioLogado.getId().toString())
				.setIssuedAt(hoje)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	//Verifica se está válido.
	public boolean isTokenValido(String token) {
		//Descriptografa e verifica se está correto.
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true; //Se o token está válido retorna true.
		} catch (Exception e) {
			return false;
		}
		
	}

	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

}
