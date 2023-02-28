package it.progetto.catering.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.progetto.catering.controller.validator.IngredienteValidator;
import it.progetto.catering.model.Ingrediente;
import it.progetto.catering.model.Piatto;
import it.progetto.catering.service.IngredienteService;
import it.progetto.catering.service.PiattoService;

@Controller
public class IngredienteController {

	@Autowired
	private IngredienteService ingredienteService;
	
	@Autowired
	private PiattoService PiattoService;

	@Autowired
	private IngredienteValidator ingredienteValidator;	






	@GetMapping("/ingrediente/{id}")
	public String getIngrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo persona
		return "ingrediente.html"; //la vista successiva mostra i dettagli della persona
	}

	@GetMapping("/ingredienti")
	public String getAllIngredienti(Model model) {
		List<Ingrediente> listaIngredienti = ingredienteService.findAll();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "ingredienti.html";
	}







	/*metodi di IngredienteController che riguardano l'admin*/

	@GetMapping("/admin/ingrediente/{id}")
	public String getIngredienteAdmin(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);//la stringa mi indica che nelle viste, per recuperare l'ogg lo chiamiamo persona
		return "admin/ingrediente.html"; //la vista successiva mostra i dettagli della persona
	}

	@GetMapping("/admin/ingrediente")
	public String getAllIngredientiAdmin(Model model) {
		List<Ingrediente> listaIngredienti = ingredienteService.findAll();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "admin/ingredienti.html";
	}




	@PostMapping("/admin/ingrediente")
	public String addIngredienteAdmin(@Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResult, Model model) {
		ingredienteValidator.validate(ingrediente, bindingResult);//se l'ingrediente che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. ingrediente dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			ingredienteService.save(ingrediente);
			model.addAttribute("ingrediente", ingrediente);
			return "admin/ingrediente.html";
		}
		return "admin/ingredienteForm.html";  //altrimenti ritorna alla pagina della form
	}




//	@Transactional
//	@PostMapping("/admin/modificaIngrediente/{id}")
//	public String modificaIngredienteAdmin(@PathVariable Long id, @Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResults, Model model) {
//		if(!bindingResults.hasErrors()) {
//			Ingrediente vecchioIngrediente = ingredienteService.findById(id);
//			vecchioIngrediente.setNome(ingrediente.getNome());
//			vecchioIngrediente.setDescrizione(ingrediente.getDescrizione());
//			vecchioIngrediente.setOrigine(ingrediente.getOrigine());
//			this.ingredienteService.save(vecchioIngrediente);
//			model.addAttribute("ingrediente", ingrediente);
//			return "admin/ingrediente.html";
//		} 
//		return "admin/modificaIngredientForm.html";
//	}
	@Transactional
	@PostMapping("/admin/ingredienteEdited/{id}")
	public String editedIngrediente(@PathVariable Long id, @Valid @ModelAttribute("ingrediente") Ingrediente ingrediente, BindingResult bindingResult, Model model) {

		Ingrediente oldingrediente = ingredienteService.findById(id);

		if(!ingrediente.getNome().equals(oldingrediente.getNome()))
			ingredienteValidator.validate(ingrediente, bindingResult);

		if(!bindingResult.hasErrors()) {
		oldingrediente.setNome(ingrediente.getNome());
		oldingrediente.setOrigine(ingrediente.getOrigine());
		oldingrediente.setDescrizione(ingrediente.getDescrizione());

		ingredienteService.save(oldingrediente);
		model.addAttribute("ingrediente", oldingrediente);
		return "admin/ingrediente.html";
		}
		return "admin/modificaIngredienteForm.html";
	}

	@GetMapping("/admin/modificaIngrediente/{id}")
	public String editIngrediente(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = ingredienteService.findById(id);
		model.addAttribute("ingrediente", ingrediente);
		return "admin/modificaIngredienteForm.html";
	}


	@GetMapping("/admin/toDeleteIngrediente/{id}")
	public String toDeleteIngredienteAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingrediente", ingredienteService.findById(id));
		return "admin/toDeleteIngrediente.html";
	}

	//	@Transactional
	//	@GetMapping("/admin/deleteIngrediente/{id}")
	//	public String deleteIngredienteAdmin(@PathVariable("id")Long id, Ingrediente ingrediente, BindingResult bindingResult,Model model) {
	//		ingredienteService.deleteIngredienteById(id);
	//		model.addAttribute("ingredienti", ingredienteService.findAll());
	//		return "admin/ingredienti.html";
	//	}
	@GetMapping(value="/admin/deleteIngrediente/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model) {
		Ingrediente ingrediente = this.ingredienteService.findById(id);
		List<Piatto> piatti = this.PiattoService.findAll();
		/*se i piatti contengono l'ingrediente, allora rimuovo l'ingrediente da essi*/
		for(Piatto Piatto : piatti) {
			if(Piatto.getIngredienti().contains(ingrediente)) {
				Piatto.getIngredienti().remove(ingrediente);
				this.PiattoService.save(Piatto);
			}
		}

		ingredienteService.deleteById(id);
		List<Ingrediente> listaIngredienti = this.ingredienteService.findAll();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "admin/ingredienti.html";
	}


	@GetMapping("/admin/ingredienteForm")
	public String getIngredienteFormAdmin(Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		return "admin/ingredienteForm.html";
	}




}
