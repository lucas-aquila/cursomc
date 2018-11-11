package com.lucasaquila.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.lucasaquila.cursomc.domain.Cliente;
import com.lucasaquila.cursomc.domain.enums.TipoCliente;
import com.lucasaquila.cursomc.dto.ClienteNewDTO;
import com.lucasaquila.cursomc.repositories.ClienteRepository;
import com.lucasaquila.cursomc.resources.exceptions.FieldMessage;
import com.lucasaquila.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public	void initialize(ClienteInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		// inclua os testes aqui, inserindo erros na lista
		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCpf(objDto.getCpfOuCnpj())) {
			list.add( new FieldMessage("cpfOuCnpj","CPF Inválido"));
		}
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCnpj(objDto.getCpfOuCnpj())) {
			list.add( new FieldMessage("cpfOuCnpj","CNPJ Inválido"));
		}
		
		Cliente aux = clienteRepository.findByEmail(objDto.getEmail());
		if(aux != null) {
			list.add( new FieldMessage("email", "Email já existente"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}