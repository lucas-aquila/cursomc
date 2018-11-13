package com.lucasaquila.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.lucasaquila.cursomc.domain.Pedido;

//Definida como interface, pois ela é um contrato, quais operações o serviço de email deve conter
public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
