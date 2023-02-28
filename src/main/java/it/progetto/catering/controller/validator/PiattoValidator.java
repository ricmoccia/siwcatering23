package it.progetto.catering.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.progetto.catering.model.Piatto;
import it.progetto.catering.service.PiattoService;

@Component
public class PiattoValidator implements Validator{

	@Autowired
	private PiattoService PiattoService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Piatto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.PiattoService.alreadyExists((Piatto) target)) { //se la persona gia esiste
			/*specifica che c'è stato un errore nella validazione e registra un codice di errore(stringa persona.duplicato)
			 * il codice di errore persona.duplicato è associato ad un messaggio nel file messages_IT.properties*/
			errors.reject("Piatto.duplicato");
		}
	}

}
