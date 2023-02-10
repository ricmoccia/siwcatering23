package it.progetto.catering.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.progetto.catering.model.Chef;
import it.progetto.catering.service.ChefService;

@Component
public class ChefValidator implements Validator{

	@Autowired
	private ChefService chefService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Chef.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.chefService.alreadyExists((Chef) target)) { //se la persona gia esiste
			/*specifica che c'è stato un errore nella validazione e registra un codice di errore(stringa persona.duplicato)
			 * il codice di errore persona.duplicato è associato ad un messaggio nel file messages_IT.properties*/
			errors.reject("chef.duplicato");
		}
	}

}
