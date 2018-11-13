package com.lucasaquila.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.lucasaquila.cursomc.security.UserSS;

public class UserService {
	//Método é statico, pois, pode ser chamado independente se a classe foi instanciada
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
