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

import it.progetto.catering.controller.validator.PiattoValidator;
import it.progetto.catering.model.Buffet;
import it.progetto.catering.model.Chef;
import it.progetto.catering.model.Ingrediente;
import it.progetto.catering.model.Piatto;
import it.progetto.catering.service.BuffetService;
import it.progetto.catering.service.IngredienteService;
import it.progetto.catering.service.PiattoService;

@Controller
public class PiattoController {

	@Autowired
	private PiattoService PiattoService;

	@Autowired
	private BuffetService buffetService;

	@Autowired
	private IngredienteService ingredienteService;

	@Autowired
	private PiattoValidator PiattoValidator;	




	@GetMapping("/Piatto/{id}")
	public String getPiatto(@PathVariable("id") Long id, Model model) {
		Piatto Piatto = PiattoService.findById(id);
		model.addAttribute("Piatto", Piatto);
		model.addAttribute("listaBuffet", Piatto.getBuffets());
		model.addAttribute("listaIngredienti", Piatto.getIngredienti());
		return "piatto.html"; //la vista successiva mostra i dettagli del Piatto
	}

	@GetMapping("/piatti")
	public String getAllPiatti(Model model) {
		List<Piatto> listaPiatti = PiattoService.findAll();
		model.addAttribute("listaPiatti", listaPiatti);
		return "piatti.html";
	}




	/*metodi di PiattoController relativi all'admin*/



	@GetMapping("/admin/Piatto/{id}")
	public String getPiattoAdmin(@PathVariable("id") Long id, Model model) {
		Piatto Piatto = PiattoService.findById(id);
		model.addAttribute("Piatto", Piatto);
		model.addAttribute("listaBuffet", Piatto.getBuffets());
		model.addAttribute("listaIngredienti", Piatto.getIngredienti());
		return "admin/piatto.html"; //la vista successiva mostra i dettagli del Piatto
	}

	@GetMapping("/admin/Piatto")
	public String getAllPiattiAdmin(Model model) {
		List<Piatto> listaPiatti = PiattoService.findAll();
		model.addAttribute("listaPiatti", listaPiatti);
		return "admin/piatti.html";
	}

	/*Questo metodo che ritorna la form, prima di ritornarla, mette nel modello un ogg Piatto appena creato*/
	@GetMapping("/admin/PiattoForm")
	public String getPiattoFormAdmin(Model model) {
		//in questo modo PiattoForm ha un ogg Piatto a disposizione(senza questa op. non l'avrebbe avuto)
		model.addAttribute("Piatto", new Piatto());
		return "admin/piattoForm.html"; 		
	}	


	@PostMapping("/admin/Piatto")
	public String addPiattoAdmin(@Valid @ModelAttribute("Piatto") Piatto Piatto, BindingResult bindingResult, Model model) {
		PiattoValidator.validate(Piatto, bindingResult);//se lo chef che cerco di inserire e gia presente annullo l'inserimento, bindingResult da l'errore
		//prima di salvare l'ogg. persona dobbiamo verificare che non ci siano stati errori di validazione
		if(!bindingResult.hasErrors()) {//se non ci sono stati err di validazione
			PiattoService.save(Piatto);
			model.addAttribute("Piatto", Piatto);
			return "admin/piatto.html";
		}
		else {
			List<Ingrediente> listaIngredienti= ingredienteService.findAll();
			model.addAttribute("listaIngredienti", listaIngredienti);
			return "admin/piattoForm.html";  //altrimenti ritorna alla pagina della form
		}
	}


	@Transactional
	@PostMapping("/admin/PiattoEdited/{id}")
	public String modificaPiattoAdmin(@PathVariable Long id, @Valid @ModelAttribute("Piatto") Piatto Piatto, BindingResult bindingResults, Model model) {
		if(!bindingResults.hasErrors()) {
			Piatto vecchioPiatto = PiattoService.findById(id);
			vecchioPiatto.setNome(Piatto.getNome());
			vecchioPiatto.setDescrizione(Piatto.getDescrizione());
			this.PiattoService.save(vecchioPiatto);
			model.addAttribute("Piatto", Piatto);
			return "admin/piatto.html";
		} 
		else {
			model.addAttribute("listaIngredienti", ingredienteService.findAll());
			return "admin/modificaPiattoForm.html";
		}
	}	
	@GetMapping("/admin/modificaPiatto/{id}")
	public String getPiattoFormAdmin(@PathVariable Long id, Model model) {
		model.addAttribute("Piatto", PiattoService.findById(id));
		return "admin/modificaPiattoForm.html";
	}

	@GetMapping("/admin/toDeletePiatto/{id}")
	public String toDeletePiattoAdmin(@PathVariable("id") Long id, Model model) {
		model.addAttribute("Piatto", PiattoService.findById(id));
		return "admin/toDeletePiatto.html";
	}

	//	@Transactional
	//	@GetMapping("/admin/deletePiatto/{id}")
	//	public String deleteChefAdmin(@PathVariable("id")Long id, Piatto Piatto, BindingResult bindingResult,Model model) {
	//		PiattoService.deletePiattoById(id);
	//		model.addAttribute("piatti", PiattoService.findAll());
	//		return "admin/piatti.html";
	//	}

	/*quando elimino un Piatto, lo devo eliminare anche dalla lista di piatti nel buffet*/
	@Transactional
	@GetMapping(value="/admin/deletePiatto/{id}")
	public String deleteBuffet(@PathVariable("id") Long id, Model model) {
		Piatto Piatto = this.PiattoService.findById(id);
		List<Buffet> elencoBuffet = Piatto.getBuffets();
		for(Buffet buffet : elencoBuffet) {
			if(buffet.getPiatti().contains(Piatto)) {
				buffet.getPiatti().remove(Piatto);
				this.buffetService.save(buffet);
			}
		}
		PiattoService.deletePiattoById(id);
		List<Piatto> listaPiatti = this.PiattoService.findAll();
		model.addAttribute("listaPiatti", listaPiatti);
		return "admin/piatti.html";
	}






	@GetMapping("admin/PiattoAddIngrediente/{id}")
	public String buffetAddIngredienteAdmin(@PathVariable("id") Long id, Model model) {
		Piatto Piatto= this.PiattoService.findById(id);
		List<Ingrediente> ingredientiNelPiatto= Piatto.getIngredienti();
		List<Ingrediente> listaIngredienti = this.ingredienteService.findAll();
		listaIngredienti.removeAll(ingredientiNelPiatto); //ottengo tutti gli ingredienti meno quello presenti nel Piatto
		model.addAttribute("Piatto", Piatto);
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "admin/piattoAddIngrediente.html";
	}
	/*aggiungo un ingrediente al Piatto*/ 
	@PostMapping("admin/addIngrediente/{idPiatto}/{idIngrediente}")
	public String addIngredienteAdmin(@PathVariable("idPiatto") Long idPiatto, @PathVariable("idIngrediente") Long idIngrediente, Model model) {
		Piatto Piatto = this.PiattoService.findById(idPiatto);
		model.addAttribute("Piatto", Piatto);
		Ingrediente ingrediente = this.ingredienteService.findById(idIngrediente);
		if(!Piatto.getIngredienti().contains(ingrediente)) {
			Piatto.getIngredienti().add(ingrediente);
		}
		PiattoService.save(Piatto);
		List <Ingrediente> listaIngredienti = Piatto.getIngredienti();
		model.addAttribute("listaIngredienti", listaIngredienti);
		return "admin/piatto.html";
	}







}
