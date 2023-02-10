package it.progetto.catering.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.progetto.catering.model.Materiale;
import it.progetto.catering.service.IngredienteService;

@Component
public class IngredienteValidator implements Validator{

	@Autowired
	private IngredienteService ingredienteService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Materiale.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if(this.ingredienteService.alreadyExists((Materiale) target)) { //se la persona gia esiste
			/*specifica che c'è stato un errore nella validazione e registra un codice di errore(stringa persona.duplicato)
			 * il codice di errore persona.duplicato è associato ad un messaggio nel file messages_IT.properties*/
			errors.reject("ingrediente.duplicato");
		}
	}

}
