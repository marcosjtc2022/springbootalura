package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.repository.UsuarioRepository;

//Como a segurança é bastante dinâmica, é melhor configurar na classe e não no application.
//<artifactId>spring-boot-starter-security</artifactId>
@EnableWebSecurity //Habilita o módulo de segurança da aplicação.
@Configuration //Ao iniciar o projeto o spring procura as configurações nesta classe.
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	//Configuração de autenticação. Controle de acesso, login, etc.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//Configuração de autorização - Url, quem pode acessar cada url, perfil de acesso.
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers(HttpMethod.GET, "/topicos" ).permitAll()
		.antMatchers(HttpMethod.GET, "/topicos/*" ).permitAll()
		.antMatchers(HttpMethod.GET, "/actuator/**" ).permitAll() //Não colocar em produção.
		.antMatchers(HttpMethod.POST, "/auth" ).permitAll()
		.antMatchers(HttpMethod.GET, "/swagger-ui.html" ).permitAll()
		.anyRequest().authenticated() ///topicos/{id}
		.and().csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //usado para autenticar com token.
		.and().addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioRepository), UsernamePasswordAuthenticationFilter.class); //Spring precisa saber qual a ordem dos filtros, pois, tem um filtro interno de autenticação.
		//Passa o filtro que quer adicionar e antes de qual filtro quer executar.
		//Antes de fazer qualquer coisa, roda o filtro para pegar o token.
		//.and().formLogin(); //Spring gera um formulário de autenticação que recebe as requisições deste formulário.
		//http.headers().frameOptions().sameOrigin();
	
	}
	
	//Configuração de recursos estáticos (js, css, imagens). Front end!
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**.html", "/v2/api-docs", "/webjars/**", "/configuration/**", "/swagger-resources/**");
	}
	
	 @Override
	 @Bean
	 public AuthenticationManager authenticationManagerBean() throws Exception {
	     return super.authenticationManagerBean();
	 }

}
