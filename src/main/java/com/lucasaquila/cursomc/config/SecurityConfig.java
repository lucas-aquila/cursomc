package com.lucasaquila.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.lucasaquila.cursomc.security.JWTAuthenticationFilter;
import com.lucasaquila.cursomc.security.JWTAuthorizationFilter;
import com.lucasaquila.cursomc.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	//Instanciamos a interface UserDetailsService, mas o Spring busca por uma implementação dela. 
	//No caso implementamos a UserDetailsServiceImpl
	@Autowired
	private UserDetailsService userDetailsService;
	
	//Define quais caminhos estarão liberados para acesso por padrão.
	//O ** define que a partir daquele caminho em diante estão liberados
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**",
	};
	
	//Define quais caminhos estarão liberados para acesso por padrão, apenas no método GET, ou seja, apenas leitura.
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**",
			"/estados/**",
	};
	
	//Usuário não está logado mas tem permissão para fazer um POST, por exemplo, se cadastrar em uma loja online
	private static final String[] PUBLIC_MATCHERS_POST = {
			"/clientes",
			"/auth/forgot/**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//Se nos profiles ativos existir o test, permite que acessamos o H2, é uma peculiaridade que precisou incluir
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		//Utiliza as configurações padrões definidas do cors no método corsConfigurationSource
		//Desabilita o csrf pois o sistema é stateless, não armazena dados da autenticação em seção.
		http.cors().and().csrf().disable();

		//Vai permitir o acesso a todos que estiverem no antMatchers().permitAll()
		//E o restante anyRequest() irá exigir autenticação
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
		.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
		.antMatchers(PUBLIC_MATCHERS).permitAll()
		.anyRequest().authenticated();
		
		//Filtro de autenticação
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		
		//Filtro de autorização
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		
		//Irá garantir que nosso backend não vai criar seção de usuário
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	
	//Definindo um bean com acesso básico de multiplas fontes( applyPermitDefaultValues ) para todos os caminhos
	//Está permitindo acesso aos endpoints (controllers) com as configurações básicas
	//Se existir um metodo corsConfigurationSource implementado ele irá chamar o http.cors com estas configurações
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		configuration.setAllowedMethods(Arrays.asList("POST","GET","PUT", "DELETE", "OPTIONS"));
		return source;
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		 web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**",
		"/swagger-ui.html", "/webjars/**");
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
