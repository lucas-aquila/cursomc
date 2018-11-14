package com.lucasaquila.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.lucasaquila.cursomc.domain.Cliente;
import com.lucasaquila.cursomc.repositories.ClienteRepository;
import com.lucasaquila.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	private Random random = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassowrd();
		cliente.setSenha(bCryptPasswordEncoder.encode(newPass));
		
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassowrd() {
		char[] vet = new char[10];
		for(int i=0; i <10; i++){
			vet[i] = randomChar();
		}
		return vet.toString();
	}

	private char randomChar() {
		//Busca um valor randomico de 0 a 2
		int opt = random.nextInt(3);
		if(opt == 0) { // gera um digito
			return (char) (random.nextInt(10) + 48); // Os digitos na tabela unicode vão do 48 ao 57, gera um random de 0 a 9 e soma por 48
		} else if (opt == 1) { // gera letra maiscula
			return (char) (random.nextInt(26) + 65);
		} else { // gera letra minuscula
			return (char) (random.nextInt(26) + 97);
		}
	}
}
